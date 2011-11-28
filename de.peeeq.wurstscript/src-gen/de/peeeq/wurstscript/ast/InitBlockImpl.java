//generated by parseq
package de.peeeq.wurstscript.ast;

class InitBlockImpl implements InitBlock, AstElementIntern {
	InitBlockImpl(WPos source, WStatements body) {
		if (source == null) throw new IllegalArgumentException();
		((AstElementIntern)source).setParent(this);
		this.source = source;
		if (body == null) throw new IllegalArgumentException();
		((AstElementIntern)body).setParent(this);
		this.body = body;
	}

	private AstElement parent;
	public AstElement getParent() { return parent; }
	public void setParent(AstElement parent) {
		if (parent != null && this.parent != null) { 			throw new Error("Parent of " + this + " already set: " + this.parent + "\ntried to change to " + parent); 		}
		this.parent = parent;
	}

	private WPos source;
	public void setSource(WPos source) {
		if (source == null) throw new IllegalArgumentException();
		((AstElementIntern)this.source).setParent(null);
		((AstElementIntern)source).setParent(this);
		this.source = source;
	} 
	public WPos getSource() { return source; }

	private WStatements body;
	public void setBody(WStatements body) {
		if (body == null) throw new IllegalArgumentException();
		((AstElementIntern)this.body).setParent(null);
		((AstElementIntern)body).setParent(this);
		this.body = body;
	} 
	public WStatements getBody() { return body; }

	public AstElement get(int i) {
		switch (i) {
			case 0: return source;
			case 1: return body;
			default: throw new IllegalArgumentException("Index out of range: " + i);
		}
	}
	public int size() {
		return 2;
	}
	public InitBlock copy() {
		return new InitBlockImpl(source.copy(), body.copy());
	}
	@Override public void accept(WScope.Visitor v) {
		source.accept(v);
		body.accept(v);
		v.visit(this);
	}
	@Override public void accept(WEntities.Visitor v) {
		source.accept(v);
		body.accept(v);
		v.visit(this);
	}
	@Override public void accept(TopLevelDeclaration.Visitor v) {
		source.accept(v);
		body.accept(v);
		v.visit(this);
	}
	@Override public void accept(CompilationUnit.Visitor v) {
		source.accept(v);
		body.accept(v);
		v.visit(this);
	}
	@Override public void accept(WEntity.Visitor v) {
		source.accept(v);
		body.accept(v);
		v.visit(this);
	}
	@Override public void accept(PackageOrGlobal.Visitor v) {
		source.accept(v);
		body.accept(v);
		v.visit(this);
	}
	@Override public void accept(InitBlock.Visitor v) {
		source.accept(v);
		body.accept(v);
		v.visit(this);
	}
	@Override public void accept(WPackage.Visitor v) {
		source.accept(v);
		body.accept(v);
		v.visit(this);
	}
	@Override public <T> T match(WEntity.Matcher<T> matcher) {
		return matcher.case_InitBlock(this);
	}
	@Override public void match(WEntity.MatcherVoid matcher) {
		matcher.case_InitBlock(this);
	}

	@Override public <T> T match(WScope.Matcher<T> matcher) {
		return matcher.case_InitBlock(this);
	}
	@Override public void match(WScope.MatcherVoid matcher) {
		matcher.case_InitBlock(this);
	}

	@Override public String toString() {
		return "InitBlock(" + source + ", " +body+")";
	}
	private boolean attr_attrScopeNames_isCached = false;
	private java.util.Map<String, NameDef> attr_attrScopeNames_cache;
	public java.util.Map<String, NameDef> attrScopeNames() {
		if (!attr_attrScopeNames_isCached) {
			attr_attrScopeNames_cache = de.peeeq.wurstscript.attributes.AttrScopeNames.calculate(this);
			attr_attrScopeNames_isCached = true;
		}
		return attr_attrScopeNames_cache;
	}
	private boolean attr_attrScopePackageNames_isCached = false;
	private java.util.Map<String, NameDef> attr_attrScopePackageNames_cache;
	public java.util.Map<String, NameDef> attrScopePackageNames() {
		if (!attr_attrScopePackageNames_isCached) {
			attr_attrScopePackageNames_cache = de.peeeq.wurstscript.attributes.AttrScopeNames.calculatePackage(this);
			attr_attrScopePackageNames_isCached = true;
		}
		return attr_attrScopePackageNames_cache;
	}
	private boolean attr_attrScopePublicNames_isCached = false;
	private java.util.Map<String, NameDef> attr_attrScopePublicNames_cache;
	public java.util.Map<String, NameDef> attrScopePublicNames() {
		if (!attr_attrScopePublicNames_isCached) {
			attr_attrScopePublicNames_cache = de.peeeq.wurstscript.attributes.AttrScopeNames.calculatePublic(this);
			attr_attrScopePublicNames_isCached = true;
		}
		return attr_attrScopePublicNames_cache;
	}
	private boolean attr_attrScopePublicReadNamess_isCached = false;
	private java.util.Map<String, NameDef> attr_attrScopePublicReadNamess_cache;
	public java.util.Map<String, NameDef> attrScopePublicReadNamess() {
		if (!attr_attrScopePublicReadNamess_isCached) {
			attr_attrScopePublicReadNamess_cache = de.peeeq.wurstscript.attributes.AttrScopeNames.calculatePublicRead(this);
			attr_attrScopePublicReadNamess_isCached = true;
		}
		return attr_attrScopePublicReadNamess_cache;
	}
	private boolean attr_attrScopeFunctions_isCached = false;
	private com.google.common.collect.Multimap<String, de.peeeq.wurstscript.attributes.FuncDefInstance> attr_attrScopeFunctions_cache;
	public com.google.common.collect.Multimap<String, de.peeeq.wurstscript.attributes.FuncDefInstance> attrScopeFunctions() {
		if (!attr_attrScopeFunctions_isCached) {
			attr_attrScopeFunctions_cache = de.peeeq.wurstscript.attributes.AttrScopeFunctions.calculate(this);
			attr_attrScopeFunctions_isCached = true;
		}
		return attr_attrScopeFunctions_cache;
	}
	private boolean attr_attrScopePackageFunctions_isCached = false;
	private com.google.common.collect.Multimap<String, de.peeeq.wurstscript.attributes.FuncDefInstance> attr_attrScopePackageFunctions_cache;
	public com.google.common.collect.Multimap<String, de.peeeq.wurstscript.attributes.FuncDefInstance> attrScopePackageFunctions() {
		if (!attr_attrScopePackageFunctions_isCached) {
			attr_attrScopePackageFunctions_cache = de.peeeq.wurstscript.attributes.AttrScopeFunctions.calculatePackage(this);
			attr_attrScopePackageFunctions_isCached = true;
		}
		return attr_attrScopePackageFunctions_cache;
	}
	private boolean attr_attrScopePublicFunctions_isCached = false;
	private com.google.common.collect.Multimap<String, de.peeeq.wurstscript.attributes.FuncDefInstance> attr_attrScopePublicFunctions_cache;
	public com.google.common.collect.Multimap<String, de.peeeq.wurstscript.attributes.FuncDefInstance> attrScopePublicFunctions() {
		if (!attr_attrScopePublicFunctions_isCached) {
			attr_attrScopePublicFunctions_cache = de.peeeq.wurstscript.attributes.AttrScopeFunctions.calculatePublic(this);
			attr_attrScopePublicFunctions_isCached = true;
		}
		return attr_attrScopePublicFunctions_cache;
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
	private boolean attr_attrNearestClassOrModule_isCached = false;
	private ClassOrModule attr_attrNearestClassOrModule_cache;
	public ClassOrModule attrNearestClassOrModule() {
		if (!attr_attrNearestClassOrModule_isCached) {
			attr_attrNearestClassOrModule_cache = de.peeeq.wurstscript.attributes.AttrNearestClassDef.nearestClassOrModule(this);
			attr_attrNearestClassOrModule_isCached = true;
		}
		return attr_attrNearestClassOrModule_cache;
	}
}