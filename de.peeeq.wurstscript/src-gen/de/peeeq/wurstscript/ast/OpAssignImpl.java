//generated by parseq
package de.peeeq.wurstscript.ast;

class OpAssignImpl implements OpAssign, AstElementIntern {
	OpAssignImpl() {
	}

	private AstElement parent;
	public AstElement getParent() { return parent; }
	public void setParent(AstElement parent) {
		if (parent != null && this.parent != null) { 			throw new Error("Parent of " + this + " already set: " + this.parent + "\ntried to change to " + parent); 		}
		this.parent = parent;
	}

	public AstElement get(int i) {
		switch (i) {
			default: throw new IllegalArgumentException("Index out of range: " + i);
		}
	}
	public int size() {
		return 0;
	}
	public OpAssign copy() {
		return new OpAssignImpl();
	}
	@Override public void accept(WPackage.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(StmtIf.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(OpAssignment.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(OpAssign.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(ClassMember.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(TopLevelDeclaration.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(StmtWhile.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(WStatements.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(OnDestroyDef.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(WScope.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(ClassDef.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(WEntity.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(ConstructorDef.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(WStatement.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(JassToplevelDeclaration.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(ClassSlot.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(FunctionDefinition.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(TypeDef.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(ClassSlots.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(InitBlock.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(FuncDef.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(PackageOrGlobal.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(StmtLoop.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(StmtSet.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(WEntities.Visitor v) {
		v.visit(this);
	}
	@Override public void accept(CompilationUnit.Visitor v) {
		v.visit(this);
	}
	@Override public <T> T match(OpAssignment.Matcher<T> matcher) {
		return matcher.case_OpAssign(this);
	}
	@Override public void match(OpAssignment.MatcherVoid matcher) {
		matcher.case_OpAssign(this);
	}

	@Override public String toString() {
		return "OpAssign";
	}
	private boolean attr_attrNearestPackage_isCached = false;
	private PackageOrGlobal attr_attrNearestPackage_cache;
	public PackageOrGlobal attrNearestPackage() {
		if (!attr_attrNearestPackage_isCached) {
			attr_attrNearestPackage_cache = de.peeeq.wurstscript.attributes.AttrNearestPackage.calculate(this);
			attr_attrNearestPackage_isCached = true;
		}
		return attr_attrNearestPackage_cache;
	}
	private boolean attr_attrNearestFuncDef_isCached = false;
	private FuncDef attr_attrNearestFuncDef_cache;
	public FuncDef attrNearestFuncDef() {
		if (!attr_attrNearestFuncDef_isCached) {
			attr_attrNearestFuncDef_cache = de.peeeq.wurstscript.attributes.AttrNearestFuncDef.calculate(this);
			attr_attrNearestFuncDef_isCached = true;
		}
		return attr_attrNearestFuncDef_cache;
	}
	private boolean attr_attrNearestClassDef_isCached = false;
	private ClassDef attr_attrNearestClassDef_cache;
	public ClassDef attrNearestClassDef() {
		if (!attr_attrNearestClassDef_isCached) {
			attr_attrNearestClassDef_cache = de.peeeq.wurstscript.attributes.AttrNearestClassDef.calculate(this);
			attr_attrNearestClassDef_isCached = true;
		}
		return attr_attrNearestClassDef_cache;
	}
}
