//generated by parseq
package de.peeeq.wurstscript.jassAst;

class JassExprBinaryImpl implements JassExprBinary, JassAstElementIntern {
	JassExprBinaryImpl(JassExpr left, JassOpBinary op, JassExpr right) {
		if (left == null) throw new IllegalArgumentException();
		((JassAstElementIntern)left).setParent(this);
		this.left = left;
		if (op == null) throw new IllegalArgumentException();
		((JassAstElementIntern)op).setParent(this);
		this.op = op;
		if (right == null) throw new IllegalArgumentException();
		((JassAstElementIntern)right).setParent(this);
		this.right = right;
	}

	private JassAstElement parent;
	public JassAstElement getParent() { return parent; }
	public void setParent(JassAstElement parent) {
		if (parent != null && this.parent != null) { 			throw new Error("Parent of " + this + " already set: " + this.parent + "\ntried to change to " + parent); 		}
		this.parent = parent;
	}

	private JassExpr left;
	public void setLeft(JassExpr left) {
		if (left == null) throw new IllegalArgumentException();
		((JassAstElementIntern)this.left).setParent(null);
		((JassAstElementIntern)left).setParent(this);
		this.left = left;
	} 
	public JassExpr getLeft() { return left; }

	private JassOpBinary op;
	public void setOp(JassOpBinary op) {
		if (op == null) throw new IllegalArgumentException();
		((JassAstElementIntern)this.op).setParent(null);
		((JassAstElementIntern)op).setParent(this);
		this.op = op;
	} 
	public JassOpBinary getOp() { return op; }

	private JassExpr right;
	public void setRight(JassExpr right) {
		if (right == null) throw new IllegalArgumentException();
		((JassAstElementIntern)this.right).setParent(null);
		((JassAstElementIntern)right).setParent(this);
		this.right = right;
	} 
	public JassExpr getRight() { return right; }

	public JassAstElement get(int i) {
		switch (i) {
			case 0: return left;
			case 1: return op;
			case 2: return right;
			default: throw new IllegalArgumentException("Index out of range: " + i);
		}
	}
	public int size() {
		return 3;
	}
	public JassExprBinary copy() {
		return new JassExprBinaryImpl(left.copy(), op.copy(), right.copy());
	}
	@Override public void accept(JassStmtLoop.Visitor v) {
		left.accept(v);
		op.accept(v);
		right.accept(v);
		v.visit(this);
	}
	@Override public void accept(JassStatements.Visitor v) {
		left.accept(v);
		op.accept(v);
		right.accept(v);
		v.visit(this);
	}
	@Override public void accept(JassExprAtomic.Visitor v) {
		left.accept(v);
		op.accept(v);
		right.accept(v);
		v.visit(this);
	}
	@Override public void accept(JassExpr.Visitor v) {
		left.accept(v);
		op.accept(v);
		right.accept(v);
		v.visit(this);
	}
	@Override public void accept(JassExprBinary.Visitor v) {
		left.accept(v);
		op.accept(v);
		right.accept(v);
		v.visit(this);
	}
	@Override public void accept(JassStmtSet.Visitor v) {
		left.accept(v);
		op.accept(v);
		right.accept(v);
		v.visit(this);
	}
	@Override public void accept(JassExprFunctionCall.Visitor v) {
		left.accept(v);
		op.accept(v);
		right.accept(v);
		v.visit(this);
	}
	@Override public void accept(JassProg.Visitor v) {
		left.accept(v);
		op.accept(v);
		right.accept(v);
		v.visit(this);
	}
	@Override public void accept(JassExprUnary.Visitor v) {
		left.accept(v);
		op.accept(v);
		right.accept(v);
		v.visit(this);
	}
	@Override public void accept(JassExprlist.Visitor v) {
		left.accept(v);
		op.accept(v);
		right.accept(v);
		v.visit(this);
	}
	@Override public void accept(JassFunction.Visitor v) {
		left.accept(v);
		op.accept(v);
		right.accept(v);
		v.visit(this);
	}
	@Override public void accept(JassStmtSetArray.Visitor v) {
		left.accept(v);
		op.accept(v);
		right.accept(v);
		v.visit(this);
	}
	@Override public void accept(JassExprVarArrayAccess.Visitor v) {
		left.accept(v);
		op.accept(v);
		right.accept(v);
		v.visit(this);
	}
	@Override public void accept(JassStatement.Visitor v) {
		left.accept(v);
		op.accept(v);
		right.accept(v);
		v.visit(this);
	}
	@Override public void accept(JassFunctions.Visitor v) {
		left.accept(v);
		op.accept(v);
		right.accept(v);
		v.visit(this);
	}
	@Override public void accept(JassStmtReturn.Visitor v) {
		left.accept(v);
		op.accept(v);
		right.accept(v);
		v.visit(this);
	}
	@Override public void accept(JassStmtIf.Visitor v) {
		left.accept(v);
		op.accept(v);
		right.accept(v);
		v.visit(this);
	}
	@Override public void accept(JassStmtExitwhen.Visitor v) {
		left.accept(v);
		op.accept(v);
		right.accept(v);
		v.visit(this);
	}
	@Override public void accept(JassStmtCall.Visitor v) {
		left.accept(v);
		op.accept(v);
		right.accept(v);
		v.visit(this);
	}
	@Override public <T> T match(JassExpr.Matcher<T> matcher) {
		return matcher.case_JassExprBinary(this);
	}
	@Override public void match(JassExpr.MatcherVoid matcher) {
		matcher.case_JassExprBinary(this);
	}

	@Override public String toString() {
		return "JassExprBinary(" + left + ", " +op + ", " +right+")";
	}
}