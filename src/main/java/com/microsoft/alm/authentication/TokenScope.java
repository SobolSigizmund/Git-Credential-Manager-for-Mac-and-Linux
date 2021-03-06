// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See License.txt in the project root.

package com.microsoft.alm.authentication;

import com.microsoft.alm.helpers.ScopeSet;
import com.microsoft.alm.helpers.StringHelper;

public abstract class TokenScope
{
    private static final String[] EmptyStringArray = new String[0];

    protected TokenScope(final String value)
    {
        if (StringHelper.isNullOrWhiteSpace(value))
        {
            _scopes = new String[0];
        }
        else
        {
            _scopes = new String[1];
            _scopes[0] = value;
        }
    }

    protected TokenScope(final String[] values)
    {
        _scopes = values;
    }

    protected TokenScope(final ScopeSet set)
    {
        //noinspection ToArrayCallWithZeroLengthArrayArgument
        _scopes = set.toArray(EmptyStringArray);
    }

    public String getValue()
    {
        return StringHelper.join(" ", _scopes);
    }

    protected final String[] _scopes;

    @Override public boolean equals(final Object obj)
    {
        return operatorEquals(this, obj instanceof TokenScope ? ((TokenScope) obj) : null);
    }

    @Override public int hashCode()
    {
        // largest 31-bit prime (https://msdn.microsoft.com/en-us/library/Ee621251.aspx)
        int hash = 2147483647;

        for (int i = 0; i < _scopes.length; i++)
        {
            // PORT NOTE: Java doesn't have unchecked blocks; the default behaviour is apparently equivalent.
            {
                hash ^= _scopes[i].hashCode();
            }
        }

        return hash;
    }

    @Override public String toString()
    {
        return getValue();
    }

    public static boolean operatorEquals(final TokenScope scope1, final TokenScope scope2)
    {
        if (scope1 == scope2)
            return true;
        if ((scope1 == null) || (null == scope2))
            return false;

        final ScopeSet set = new ScopeSet();
        set.unionWith(scope1._scopes);
        return set.setEquals(scope2._scopes);
    }

    public static boolean operatorNotEquals(final TokenScope scope1, final TokenScope scope2)
    {
        return !operatorEquals(scope1, scope2);
    }
}
