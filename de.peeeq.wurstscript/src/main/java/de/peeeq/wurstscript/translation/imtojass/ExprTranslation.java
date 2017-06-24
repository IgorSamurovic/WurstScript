package de.peeeq.wurstscript.translation.imtojass;


import de.peeeq.wurstscript.WurstOperator;
import de.peeeq.wurstscript.jassAst.*;
import de.peeeq.wurstscript.jassAst.JassExprVarAccess;
import de.peeeq.wurstscript.jassAst.JassExprVarArrayAccess;
import de.peeeq.wurstscript.jassAst.JassExprlist;
import de.peeeq.wurstscript.jassIm.*;
import de.peeeq.wurstscript.translation.imtranslation.ImTranslator;

import static de.peeeq.wurstscript.jassAst.JassAst.*;

public class ExprTranslation {

    public static JassExpr translate(ImBoolVal e, ImToJassTranslator translator) {
        return JassAst.JassExprBoolVal(e.getValB());
    }

    public static JassExpr translate(ImFuncRef e, ImToJassTranslator translator) {
        JassFunctionOrNative f = translator.getJassFuncFor(e.getFunc());
        return JassAst.JassExprFuncRef(f.getName());
    }

    public static JassExpr translate(ImFunctionCall e, ImToJassTranslator translator) {

        JassFunctionOrNative f = translator.getJassFuncFor(e.getFunc());
        JassExprlist arguments = JassExprlist();
        for (ImExpr arg : e.getArguments()) {
            arguments.add(arg.translate(translator));
        }
        String funcName = f.getName();
        if (funcName.equals(ImTranslator.$DEBUG_PRINT)) {
            funcName = "BJDebugMsg";
        }
        switch (e.getCallType()) {
            case NORMAL:
                return JassAst.JassExprFunctionCall(funcName, arguments);
            case EXECUTE:
                return JassAst.JassExprFunctionCall("ExecuteFunc", JassAst.JassExprlist(JassAst.JassExprStringVal(f.getName())));
            default:
                throw new Error("unhandled case");
        }
    }

    public static JassExpr translate(ImIntVal e, ImToJassTranslator translator) {
        return JassExprIntVal(String.valueOf(e.getValI()));
    }

    public static JassExpr translate(ImNull e, ImToJassTranslator translator) {
        return JassExprNull();
    }

    public static JassExpr translate(ImOperatorCall e, ImToJassTranslator translator) {
        WurstOperator op = e.getOp();
        if (op.isBinaryOp() && e.getArguments().size() == 2) {
            JassExpr left = e.getArguments().get(0).translate(translator);
            JassExpr right = e.getArguments().get(1).translate(translator);

            if (op == WurstOperator.MOD_REAL) {
                return JassExprFunctionCall("ModuloReal", JassExprlist(left, right));
            } else if (op == WurstOperator.MOD_INT) {
                return JassExprFunctionCall("ModuloInteger", JassExprlist(left, right));
            }

            return JassExprBinary(left, op.jassTranslateBinary(), right);
        } else if (op.isUnaryOp() && e.getArguments().size() == 1) {
            return JassExprUnary(op.jassTranslateUnary(), e.getArguments().get(0).translate(translator));
        } else {
            throw new Error("not implemented: " + e);
        }
    }


    public static JassExpr translate(ImRealVal e, ImToJassTranslator translator) {
        return JassExprRealVal(e.getValR());
    }

    public static JassExpr translate(ImStatementExpr e, ImToJassTranslator translator) {
        throw new Error("this expr should have been flattened: " + e);
    }

    public static JassExpr translate(ImStringVal e, ImToJassTranslator translator) {
        return JassExprStringVal(e.getValS());
    }

    public static JassExpr translate(ImTupleExpr e, ImToJassTranslator translator) {
        throw new Error("tuples should be eliminated in this phase");
    }

    public static JassExpr translate(ImTupleSelection e, ImToJassTranslator translator) {
        throw new Error("tuples should be eliminated in this phase");
    }

    public static JassExprVarAccess translate(ImVarAccess e, ImToJassTranslator translator) {
        JassVar v = translator.getJassVarFor(e.getVar());
        return JassExprVarAccess(v.getName());
    }

    public static JassExprVarArrayAccess translate(ImVarArrayAccess e, ImToJassTranslator translator) {
        JassVar v = translator.getJassVarFor(e.getVar());
        return JassExprVarArrayAccess(v.getName(), e.getIndex().translate(translator));
    }

    public static JassExpr translate(ImClassRelatedExpr e,
                                     ImToJassTranslator translator) {
        throw new RuntimeException("Eliminate method calls before translating to jass");
    }

    public static JassExpr translate(
            ImVarArrayMultiAccess imVarArrayMultiAccess,
            ImToJassTranslator translator) {
        throw new Error("not implemented");
    }

    public static JassExpr translate(ImGetStackTrace imGetStackTrace, ImToJassTranslator translator) {
        return JassAst.JassExprStringVal("");
    }


}