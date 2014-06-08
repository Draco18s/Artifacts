package com.draco18s.artifacts.arrowtrapbehaviors;

public class RegistryDefaulted extends RegistrySimple
{
    /**
     * Default object for this registry, returned when an object is not found.
     */
    private final Object defaultObject;

    public RegistryDefaulted(Object par1Obj)
    {
        this.defaultObject = par1Obj;
    }

    public Object func_82594_a(Object par1Obj)
    {
        Object object1 = super.func_82594_a(par1Obj);
        return object1 == null ? this.defaultObject : object1;
    }
}
