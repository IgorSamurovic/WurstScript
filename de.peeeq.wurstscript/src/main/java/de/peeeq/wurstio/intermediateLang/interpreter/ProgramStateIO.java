package de.peeeq.wurstio.intermediateLang.interpreter;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import de.peeeq.wurstio.mpq.MpqEditor;
import de.peeeq.wurstio.objectreader.*;
import de.peeeq.wurstscript.WLogger;
import de.peeeq.wurstscript.gui.WurstGui;
import de.peeeq.wurstscript.intermediatelang.interpreter.ProgramState;
import de.peeeq.wurstscript.jassIm.ImProg;
import de.peeeq.wurstscript.jassIm.ImStmt;
import org.eclipse.jdt.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ProgramStateIO extends ProgramState {

    private static final int GENERATED_BY_WURST = 42;
    private @Nullable ImStmt lastStatement;
    private @Nullable final MpqEditor mpqEditor;
    private final Map<ObjectFileType, ObjectFile> dataStoreMap = Maps.newLinkedHashMap();
    private int id = 0;
    private final Map<String, ObjectDefinition> objDefinitions = Maps.newLinkedHashMap();
    private PrintStream outStream = System.err;
    private @Nullable Map<Integer, String> trigStrings = null;
    private final @Nullable File mapFile;

    public ProgramStateIO(@Nullable File mapFile, @Nullable MpqEditor mpqEditor, WurstGui gui, ImProg prog, boolean isCompiletime) {
        super(gui, prog, isCompiletime);
        this.mapFile = mapFile;
        this.mpqEditor = mpqEditor;
    }

    @Override
    public void setLastStatement(ImStmt s) {
        lastStatement = s;
    }

    @Override
    public @Nullable ImStmt getLastStatement() {
        return lastStatement;
    }

    @Override
    public WurstGui getGui() {
        return super.getGui();
    }

    private String getTrigString(int id) {
        return loadTrigStrings().getOrDefault(id, "");
    }

    private Map<Integer, String> loadTrigStrings() {
        Map<Integer, String> res = trigStrings;
        if (res != null) {
            return res;
        }
        try {
            byte[] wts = mpqEditor.extractFile("war3map.wts");
            res = WTSFile.parse(wts);
        } catch (Exception e) {
            // dummy result
            res = new LinkedHashMap<>();
            WLogger.warning("Could not load trigger strings");
            WLogger.info(e);
        }
        trigStrings = res;
        return res;
    }

    ObjectFile getDataStore(String fileExtension) {
        return getDataStore(ObjectFileType.fromExt(fileExtension));
    }

    private ObjectFile getDataStore(ObjectFileType filetype) throws Error {
        ObjectFile dataStore = dataStoreMap.get(filetype);
        if (dataStore != null) {
            return dataStore;
        }
        if (mpqEditor == null) {
            // without a map: create empty object file
            dataStore = new ObjectFile(filetype);
            dataStoreMap.put(filetype, dataStore);
            return dataStore;
        }

        try {
            // extract specific object file:
            String fileName = "war3map." + filetype.getExt();
            try {
                if (mpqEditor.hasFile(fileName)) {
                    byte[] w3_ = mpqEditor.extractFile(fileName);
                    dataStore = new ObjectFile(w3_, filetype);
                    replaceTrigStrings(dataStore);
                } else {
                    dataStore = new ObjectFile(filetype);
                }
            } catch (IOException | InterruptedException e) {
                // TODO maybe tell the user, that something has gone wrong
                WLogger.info("Could not extract file: " + fileName);
                WLogger.info(e);
                dataStore = new ObjectFile(filetype);
            }
            dataStoreMap.put(filetype, dataStore);

            // clean datastore, remove all objects created by wurst
            deleteWurstObjects(dataStore);
        } catch (Exception e) {
            WLogger.severe(e);
            throw new Error(e);
        }

        return dataStore;
    }

    private void replaceTrigStrings(ObjectFile dataStore) {
        replaceTrigStringsInTable(dataStore.getOrigTable());
        replaceTrigStringsInTable(dataStore.getModifiedTable());


    }

    private void replaceTrigStringsInTable(ObjectTable modifiedTable) {
        for (ObjectDefinition od : modifiedTable.getObjectDefinitions()) {
            for (ObjectModification<?> mod : od.getModifications()) {
                if (mod instanceof ObjectModificationString) {
                    ObjectModificationString modS = (ObjectModificationString) mod;
                    if (modS.getData().startsWith("TRIGSTR_")) {
                        try {
                            int id = Integer.parseInt(modS.getData().substring("TRIGSTR_".length()), 10);
                            String newVal = getTrigString(id);
                            modS.setData(newVal);
                        } catch (NumberFormatException e) {
                            // ignore
                        }
                    }
                }
            }
        }
    }

    private void deleteWurstObjects(ObjectFile unitStore) {
        Iterator<ObjectDefinition> it = unitStore.getModifiedTable().getObjectDefinitions().iterator();
        while (it.hasNext()) {
            ObjectDefinition od = it.next();
            for (ObjectModification<?> om : od.getModifications()) {
                if (om.getModificationId().equals("wurs") && om instanceof ObjectModificationInt) {
                    ObjectModificationInt om2 = (ObjectModificationInt) om;
                    if (om2.getData() == GENERATED_BY_WURST) {
                        it.remove();
                        break;
                    }
                }
            }
        }
    }


    String addObjectDefinition(ObjectDefinition objDef) {
        id++;
        String key = "obj" + id;
        objDefinitions.put(key, objDef);
        return key;
    }

    ObjectDefinition getObjectDefinition(String key) {
        return objDefinitions.get(key);
    }

    @Override
    public void writeBack(boolean inject) {
        gui.sendProgress("Writing back generated objects");

        for (ObjectFileType fileType : ObjectFileType.values()) {
            WLogger.info("Writing back " + fileType);
            ObjectFile dataStore = getDataStore(fileType);
            if (!dataStore.isEmpty()) {
                WLogger.info("Writing back filetype " + fileType);
                writebackObjectFile(dataStore, fileType, inject);
            } else {
                WLogger.info("Writing back empty for " + fileType);
            }
        }
        writeW3oFile();
    }

    private void writeW3oFile() {
        File objFile = new File(getObjectEditingOutputFolder(), "wurstCreatedObjects.w3o");
        try (BinaryDataOutputStream objFileStream = new BinaryDataOutputStream(objFile, true)) {
            objFileStream.writeInt(1); // version
            for (ObjectFileType fileType : ObjectFileType.values()) {
                ObjectFile dataStore = getDataStore(fileType);
                if (!dataStore.isEmpty()) {
                    objFileStream.writeInt(1); // exists
                    dataStore.writeTo(objFileStream);
                } else {
                    objFileStream.writeInt(0); // does not exist
                }
            }
        } catch (Error | IOException e) {
            WLogger.severe(e);
        }
    }

    private void writebackObjectFile(ObjectFile dataStore, ObjectFileType fileType, boolean inject) throws Error {
        try {
            File folder = getObjectEditingOutputFolder();

            byte[] w3u = dataStore.writeToByteArray();

            // wurst exported objects
            Files.write(dataStore.exportToWurst(fileType), new File(folder, "WurstExportedObjects_" + fileType.getExt() + ".wurst.txt"), Charsets.UTF_8);

            if (inject) {
                if (mpqEditor == null) {
                    throw new RuntimeException("Map file must be given with '-injectobjects' option.");
                }
                String filenameInMpq = "war3map." + fileType.getExt();

                mpqEditor.insertFile(filenameInMpq, w3u);
            }
        } catch (Exception e) {
            WLogger.severe(e);
            throw new Error(e);
        }

    }

    private @Nullable File getObjectEditingOutputFolder() {
        if (mapFile == null) {
            return null;
        }
        File folder = new File(mapFile.getParent(), "objectEditingOutput");
        if (!folder.exists() && !folder.mkdirs()) {
            WLogger.info("Could not create folder " + folder.getAbsoluteFile());
            return null;
        }
        return folder;
    }

    @Override
    public PrintStream getOutStream() {
        return outStream;
    }

    @Override
    public void setOutStream(PrintStream os) {
        outStream = os;
    }


}

