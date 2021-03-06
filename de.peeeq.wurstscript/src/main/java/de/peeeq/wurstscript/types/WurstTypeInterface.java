package de.peeeq.wurstscript.types;

import de.peeeq.wurstscript.ast.Element;
import de.peeeq.wurstscript.ast.InterfaceDef;
import de.peeeq.wurstscript.jassIm.ImExprOpt;
import de.peeeq.wurstscript.jassIm.ImType;
import de.peeeq.wurstscript.jassIm.JassIm;

import java.util.List;


public class WurstTypeInterface extends WurstTypeClassOrInterface {


    private final InterfaceDef interfaceDef;

//	public PscriptTypeInterface(InterfaceDef interfaceDef, boolean staticRef) {
//		super(staticRef);
//		if (interfaceDef == null) throw new IllegalArgumentException();
//		this.interfaceDef = interfaceDef;
//	}

    public WurstTypeInterface(InterfaceDef interfaceDef, List<WurstTypeBoundTypeParam> newTypes, boolean isStaticRef) {
        super(newTypes, isStaticRef);
        if (interfaceDef == null) throw new IllegalArgumentException();
        this.interfaceDef = interfaceDef;
    }

    public WurstTypeInterface(InterfaceDef interfaceDef, List<WurstTypeBoundTypeParam> newTypes) {
        super(newTypes);
        if (interfaceDef == null) throw new IllegalArgumentException();
        this.interfaceDef = interfaceDef;
    }

    @Override
    public InterfaceDef getDef() {
        return interfaceDef;
    }

    public InterfaceDef getInterfaceDef() {
        return interfaceDef;
    }

    @Override
    public String getName() {
        return getDef().getName() + printTypeParams();
    }

    @Override
    public WurstType dynamic() {
        if (isStaticRef()) {
            return new WurstTypeInterface(getInterfaceDef(), getTypeParameters(), false);
        }
        return this;
    }

    @Override
    public WurstType replaceTypeVars(List<WurstTypeBoundTypeParam> newTypes) {
        return new WurstTypeInterface(getInterfaceDef(), newTypes);
    }

    @Override
    public boolean isSubtypeOfIntern(WurstType other, Element location) {
        if (super.isSubtypeOfIntern(other, location)) {
            return true;
        }

        if (other instanceof WurstTypeInterface) {
            WurstTypeInterface other2 = (WurstTypeInterface) other;
            if (interfaceDef == other2.interfaceDef) {
                // same interface -> check if type params are equal
                return checkTypeParametersEqual(getTypeParameters(), other2.getTypeParameters(), location);
            } else {
                // test super interfaces:
                for (WurstTypeInterface extended : interfaceDef.attrExtendedInterfaces()) {
                    if (extended.isSubtypeOf(other, location)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    @Override
    public ImType imTranslateType() {
        return TypesHelper.imInt();
    }

    @Override
    public ImExprOpt getDefaultValue() {
        return JassIm.ImNull();
    }


}
