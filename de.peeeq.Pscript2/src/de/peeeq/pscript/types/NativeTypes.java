package de.peeeq.pscript.types;

import de.peeeq.pscript.attributes.AttrTypeExprType;
import de.peeeq.pscript.attributes.infrastructure.AttributeManager;
import de.peeeq.pscript.pscript.NativeType;

public class NativeTypes {
	/**
	 * returns the PscriptType for a given nativetype definition
	 */
	public static PscriptType getType_NativeType(final AttributeManager attributeManager,NativeType typeDef) {
		if (typeDef.getName().equals("int")) {
			return PScriptTypeInt.instance();
		}
		if (typeDef.getName().equals("bool")) {
			return PScriptTypeBool.instance();
		}
		if (typeDef.getName().equals("real")) {
			return PScriptTypeReal.instance();
		}
		if (typeDef.getName().equals("string")) {
			return PScriptTypeString.instance();
		}
		if (typeDef.getName().equals("code")) {
			return PScriptTypeCode.instance();
		}
		if (typeDef.getName().equals("handle")) {
			return PScriptTypeHandle.instance();
		}
		
		PscriptType superType;
		if (typeDef.getSuperName() != null) {
			superType = attributeManager.getAttValue(AttrTypeExprType.class, typeDef.getSuperName());
		} else {
			superType = PScriptTypeVoid.instance();
		}
		return PscriptNativeType.instance(typeDef.getName(), superType);
	}
}
