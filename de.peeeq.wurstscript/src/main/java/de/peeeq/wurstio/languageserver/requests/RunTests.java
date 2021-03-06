package de.peeeq.wurstio.languageserver.requests;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import de.peeeq.wurstio.CompiletimeFunctionRunner;
import de.peeeq.wurstio.jassinterpreter.NativeFunctionsIO;
import de.peeeq.wurstio.languageserver.ModelManager;
import de.peeeq.wurstio.languageserver.WFile;
import de.peeeq.wurstscript.WLogger;
import de.peeeq.wurstscript.ast.CompilationUnit;
import de.peeeq.wurstscript.ast.Element;
import de.peeeq.wurstscript.ast.FuncDef;
import de.peeeq.wurstscript.gui.WurstGui;
import de.peeeq.wurstscript.intermediatelang.interpreter.ILInterpreter;
import de.peeeq.wurstscript.intermediatelang.interpreter.ProgramState;
import de.peeeq.wurstscript.intermediatelang.interpreter.ProgramState.StackTrace;
import de.peeeq.wurstscript.jassIm.ImFunction;
import de.peeeq.wurstscript.jassIm.ImProg;
import de.peeeq.wurstscript.jassinterpreter.TestFailException;
import de.peeeq.wurstscript.jassinterpreter.TestSuccessException;
import de.peeeq.wurstscript.translation.imtranslation.FunctionFlagEnum;
import de.peeeq.wurstscript.translation.imtranslation.ImTranslator;
import de.peeeq.wurstscript.utils.Utils;
import org.eclipse.jdt.annotation.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.concurrent.*;

import static de.peeeq.wurstio.CompiletimeFunctionRunner.FunctionFlagToRun.CompiletimeFunctions;

/**
 * Created by peter on 05.05.16.
 */
public class RunTests extends UserRequest<Object> {

    private final WFile filename;
    private final int line;
    private final int column;

    private List<ImFunction> successTests = Lists.newArrayList();
    private List<TestFailure> failTests = Lists.newArrayList();

    static public class TestFailure {
        private ImFunction function;
        private final StackTrace stackTrace;
        private final String message;

        public TestFailure(ImFunction function, StackTrace stackTrace, String message) {
            Preconditions.checkNotNull(function);
            Preconditions.checkNotNull(stackTrace);
            Preconditions.checkNotNull(message);
            this.function = function;
            this.stackTrace = stackTrace;
            this.message = message;
        }

        public StackTrace getStackTrace() {
            return stackTrace;
        }

        public String getMessage() {
            return message;
        }

        public ImFunction getFunction() {
            return function;
        }

        public String getMessageWithStackFrame() {
            StringBuilder s = new StringBuilder(message);
            s.append("\n");
            stackTrace.appendTo(s);
            return s.toString();
        }
    }

    public RunTests(String filename, int line, int column) {
        this.filename = filename == null ? null : WFile.create(filename);
        this.line = line;
        this.column = column;
    }


    @Override
    public Object execute(ModelManager modelManager) {
        WLogger.info("Starting tests " + filename + ", " + line + ", " + column);
        println("Running unit tests..\n");

        CompilationUnit cu = filename == null ? null : modelManager.getCompilationUnit(filename);
        WLogger.info("test.cu = " + Utils.printElement(cu));
        FuncDef funcToTest = getFunctionToTest(cu);
        WLogger.info("test.funcToTest = " + Utils.printElement(funcToTest));


        ImProg imProg = translateProg(modelManager);
        if (imProg == null) {
            println("Could not run tests, because program did not compile.\n");
            return "Could not translate program";
        }

        runTests(imProg, funcToTest, cu);
        return "ok";
    }

    public static class TestResult {
        private final int passedTests;
        private final int totalTests;

        public TestResult(int passedTests, int totalTests) {
            this.passedTests = passedTests;
            this.totalTests = totalTests;
        }

        public int getPassedTests() {
            return passedTests;
        }

        public int getTotalTests() {
            return totalTests;
        }
    }

    public TestResult runTests(ImProg imProg, @Nullable FuncDef funcToTest, @Nullable CompilationUnit cu) {
        WurstGui gui = new TestGui();

        CompiletimeFunctionRunner cfr = new CompiletimeFunctionRunner(imProg, null, null, new TestGui(), CompiletimeFunctions);
        ILInterpreter interpreter = cfr.getInterpreter();
        ProgramState globalState = cfr.getGlobalState();
        if(globalState == null) {
            globalState = new ProgramState(gui, imProg, true);
        }
        if (interpreter == null) {
            interpreter = new ILInterpreter(imProg, gui, null, globalState);
            interpreter.addNativeProvider(new NativeFunctionsIO());
        }

        redirectInterpreterOutput(globalState);

        // first run compiletime functions
        cfr.run();
        WLogger.info("Ran compiletime functions");


        for (ImFunction f : imProg.getFunctions()) {
            if (f.hasFlag(FunctionFlagEnum.IS_TEST)) {
                Element trace = f.attrTrace();

                if (cu != null && !Utils.elementContained(trace, cu)) {
                    continue;
                }
                if (funcToTest != null && trace != funcToTest) {
                    continue;
                }


                print("Running test <" + f.attrTrace().attrNearestPackage().tryGetNameDef().getName() + "." + f.getName() + "> .. ");
                try {
                    @Nullable ILInterpreter finalInterpreter = interpreter;
                    Callable<Void> run = () -> {
                        finalInterpreter.runVoidFunc(f, null);
                        successTests.add(f);
                        println("success!");
                        return null;
                    };
                    RunnableFuture<Void> future = new FutureTask<>(run);
                    ExecutorService service = Executors.newSingleThreadExecutor();
                    service.execute(future);
                    try {
                        future.get(10, TimeUnit.SECONDS); // Wait 10 seconds for test to complete
                    } catch (TimeoutException ex) {
                        future.cancel(true);
                        throw new TestTimeOutException();
                    } catch (ExecutionException e) {
                        throw e.getCause();
                    }
                    service.shutdown();

                } catch (TestSuccessException e) {
                    successTests.add(f);
                    println("success!");
                } catch (TestFailException e) {

                    failTests.add(new TestFailure(f, interpreter.getStackFrames(), e.getMessage()));
                    println("FAILED");
                } catch (TestTimeOutException e) {
                    failTests.add(new TestFailure(f, interpreter.getStackFrames(), e.getMessage()));
                    println("FAILED - TIMEOUT (This test did not complete in 10 seconds, it might contain an endless loop)");
                } catch (Throwable e) {
                    failTests.add(new TestFailure(f, interpreter.getStackFrames(), e.toString()));
                    println("FAILED with exception:");
                    println("\t" + e.getMessage());
                }
            }
        }
        println("Tests succeeded: " + successTests.size() + "/" + (successTests.size() + failTests.size()));
        if (failTests.size() == 0) {
            println(">> All tests have passed successfully!");
        } else {
            println(">> The following tests failed:");
            for (TestFailure e : failTests) {
                println(e.getMessageWithStackFrame());
            }
        }
        WLogger.info("finished tests");
        return new TestResult(successTests.size(), successTests.size() + failTests.size());
    }


    private void redirectInterpreterOutput(ProgramState globalState) {
        OutputStream os = new OutputStream() {

            @Override
            public void write(int b) throws IOException {
                if (b > 0) {
                    println("" + (char) b);
                }
            }

            @Override
            public void write(byte b[], int off, int len) throws IOException {
                println(new String(b, off, len));
            }


        };
        globalState.setOutStream(new PrintStream(os));
    }

    protected void println(String message) {
        print(message);
        print(System.lineSeparator());
    }

    protected void print(String message) {
        System.err.print(message);
    }

    private ImProg translateProg(ModelManager modelManager) {
        ImTranslator imTranslator = new ImTranslator(modelManager.getModel(), false);
        // will ignore udg_ variables which are not found
        imTranslator.setEclipseMode(true);
        return imTranslator.translateProg();
    }


    private FuncDef getFunctionToTest(CompilationUnit cu) {
        if (filename == null || cu == null || line < 0) {
            return null;
        }
        Element e = Utils.getAstElementAtPos(cu, line, column, false);
        while (e != null) {
            if (e instanceof FuncDef) {
                return (FuncDef) e;
            }
            e = e.getParent();
        }
        return null;
    }

    public List<TestFailure> getFailTests() {
        return failTests;
    }

    public class TestGui extends WurstGui {

        @Override
        public void sendProgress(String whatsRunningNow) {
            // ignore
        }

        @Override
        public void sendFinished() {
            // ignore
        }

        @Override
        public void showInfoMessage(String message) {
            println(message + "\n");
        }

    }

    private class TestTimeOutException extends Throwable {


        @Override
        public String getMessage() {
            return "test failed with timeout (This test did not complete in 10 seconds, it might contain an endless loop)";
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }


}
