package de.peeeq.wurstscript;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;

import com.google.common.collect.Lists;

public class RunArgs {


	private List<String> files  = Lists.newArrayList();
	private @Nullable String mapFile = null;
	private @Nullable String outFile = null;
	private List<RunOption> options = Lists.newArrayList();
	private List<File> libDirs = Lists.newArrayList();
	private RunOption optionHelp;
	private RunOption optionOpt;
	private RunOption optionInline;
	private RunOption optionLocalOptimizations;
	private RunOption optionRuntests;
	private RunOption optionGui;
	private RunOption optionAbout;
	private RunOption optionHotdoc;
	private RunOption optionShowErrors;
	private RunOption optionRunCompileTimeFunctions;
	private RunOption optionStacktraces;
	private RunOption uncheckedDispatch;
	private RunOption optionNodebug;
	private RunOption optionInjectCompiletimeObjects;
	private RunOption optionExtractImports;
	private RunOption optionStartServer;
	private RunOption optionGenerateLua;
	private RunOption optionLanguageServer;
	private RunOption optionNoExtractMapScript;
	private RunOption optionInsertCDKey;

	private class RunOption {
		final String name;
		final String descr;
		final @Nullable Consumer<String> argHandler;
		boolean isSet;

		public RunOption(String name, String descr) {
			this.name = name;
			this.descr = descr;
			this.argHandler = null;
		}

		public RunOption(String name, String descr, Consumer<String> argHandler2) {
			this.name = name;
			this.descr = descr;
			this.argHandler = argHandler2;
		}
	}



	public static RunArgs defaults() {
		return new RunArgs(new String[] {});
	}

	public RunArgs(String ... args) {
		// optimization 
		optionOpt = addOption("opt", "Enable the Froptimizer. Compresses names.");
		optionInline = addOption("inline", "Enables the inliner.");
		optionLocalOptimizations = addOption("localOptimizations", "Enables local optimizations. This feature is is still experimental.");
		// debug options
		optionStacktraces = addOption("stacktraces", "Generate stacktrace information in the script (useful for debugging).");
		optionNodebug = addOption("nodebug", "Remove all error messages from the script. (Not recommended)");
		uncheckedDispatch = addOption("uncheckedDispatch", "Removes checks from method-dispatch code. With unchecked dispatch "
				+ "some programming errors like null-pointer-dereferences or accessing of destroyed objects can no longer be detected. "
				+ "It is strongly recommended to not use this option, but it can give some performance benefits.");
		// interpreter
		optionRuntests = addOption("runtests", "Run all test functions found in the scripts.");
		optionRunCompileTimeFunctions = addOption("runcompiletimefunctions", "Run all compiletime functions found in the scripts.");
		optionInjectCompiletimeObjects = addOption("injectobjects", "Injects the objects generated by compiletime functions into the map.");
		// tools
		optionHelp = addOption("help", "Prints this help message.");
		optionAbout = addOption("-about", "Show the 'about' window.");
		optionInsertCDKey = addOption("-insertKeys", "Inserts dummy CD keys into mpqs for newgen compat");
		optionStartServer = addOption("-startServer", "Starts the compilation server.");
		optionHotdoc = addOption("-hotdoc", "Generate hotdoc html documentation.");
		optionShowErrors = addOption("-showerrors", "(currently not implemented.) Show errors generated by last compile.");
		// backends
		optionGenerateLua = addOption("lua", "generate lua output");
		// other
		optionNoExtractMapScript = addOption("noExtractMapScript", "Do not extract the map script from the map and use the one from the Wurst folder instead.");
		optionGui = addOption("gui", "Show a graphical user interface (progress bar and error window).");
		addOptionWithArg("lib", "The next argument should be a library folder which is lazily added to the build.", arg -> {
			libDirs.add(new File(arg));
		});
		optionExtractImports = addOptionWithArg("-extractImports", "Extract all files from a map into a folder next to the mapp.", arg -> {
			mapFile = arg;
		});


		addOptionWithArg("out", "Outputs the compiled script to this file.", arg -> {
			outFile = arg;
		});

		optionLanguageServer = addOption("languageServer", "Starts a language server which can be used by editors to get services "
				+ "like code completion, validations, and find declaration. The communication to the "
				+ "language server is via standard input output.");

		nextArg: for (int i=0; i<args.length; i++) {
			String a = args[i];
			if (a.startsWith("-")) {
				for (RunOption o : options) {
					if (("-" + o.name).equals(a)) {
						Consumer<String> argHandler = o.argHandler;
						if (argHandler != null) {
							i++;
							argHandler.accept(args[i]);
						}
						o.isSet = true;
						continue nextArg;
					}
				}
				throw new RuntimeException("Unknown option: " + a);
			} else {
				files.add(a);
				if (a.endsWith(".w3x") || a.endsWith(".w3m")) {
					mapFile = a;
				}
			}
		}

		if (optionHelp.isSet) {
			printHelpAndExit();
		}
	}

	private RunOption addOption(String name, String descr) {
		RunOption opt = new RunOption(name, descr);
		options.add(opt);
		return opt;
	}

	private RunOption addOptionWithArg(String name, String descr, Consumer<String> argHandler) {
		RunOption opt = new RunOption(name, descr, argHandler);
		options.add(opt);
		return opt;
	}

	public RunArgs(List<String> runArgs) {
		this(runArgs.toArray(new String[runArgs.size()]));
	}

	public void printHelpAndExit() {
		System.out.println("Usage: ");
		System.out.println("wurst <options> <files>");
		System.out.println();
		System.out.println("Example: wurst -opt common.j Blizzard.j myMap.w3x");
		System.out.println("Compiles the given map with the two script files and optimizations enabled.");
		System.out.println();
		System.out.println("Options:");
		System.out.println();
		for (RunOption opt : options) {
			System.out.println("-" + opt.name);
			System.out.println("	" + opt.descr);
			System.out.println();
		}
	}

	public List<String> getFiles() {
		return files;
	}

	public boolean isOptimize() {
		return optionOpt.isSet;
	}

	public boolean isGui() {
		return optionGui.isSet;
	}

	public @Nullable String getMapFile() {
		return mapFile;
	}

	public @Nullable String getOutFile() {
		return outFile;
	}

	public boolean showAbout() {
		return optionAbout.isSet;
	}
	
	public boolean insertKeys() {
        return optionInsertCDKey.isSet;
    }

	public boolean isStartServer() {
		return optionStartServer.isSet;
	}

	public boolean showLastErrors() {
		return optionShowErrors.isSet;
	}

	public boolean isInline() {
		return optionInline.isSet;
	}

	public boolean runCompiletimeFunctions() {
		return optionRunCompileTimeFunctions.isSet;
	}



	public boolean createHotDoc() {
		return optionHotdoc.isSet;
	}

	public boolean isNullsetting() {
		return true;
	}

	public boolean isLocalOptimizations() {
		return optionLocalOptimizations.isSet;
	}

	public boolean isIncludeStacktraces() {
		return optionStacktraces.isSet;
	}

	public boolean isNoDebugMessages() {
		return optionNodebug.isSet;
	}

	public boolean isInjectObjects() {
		return optionInjectCompiletimeObjects.isSet;
	}

	public List<File> getAdditionalLibDirs() {
		return Collections.unmodifiableList(libDirs);
	}

	public void addLibs(Set<String> dependencies) {
		for (String dep : dependencies) {
			libDirs.add(new File(dep));
		}
	}

	public boolean showHelp() {
		return optionHelp.isSet;
	}

	public boolean isExtractImports() {
		return optionExtractImports.isSet;
	}

	public boolean isGenerateLua() {
		return optionGenerateLua.isSet;
	}

	public boolean isUncheckedDispatch() {
		return uncheckedDispatch.isSet;
	}

	public boolean isLanguageServer() {
		return optionLanguageServer.isSet;
	}

	public boolean isNoExtractMapScript() {
		return optionNoExtractMapScript.isSet;
	}
}
