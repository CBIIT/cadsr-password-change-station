/*L
 * Copyright SAIC-F Inc.
 *
 * Distributed under the OSI-approved BSD 3-Clause License.
 * See http://ncip.github.com/cadsr-password-change/LICENSE.txt for details.
 */

package gov.nih.nci.cadsr.cadsrpasswordchange.domain;


/**
 * This is the parent of all DTO classes.
 */
public abstract class AbstractDto implements java.io.Serializable {

    /**
     * The Serializable version is forced to -1.
     * This helps to overcome compatibility problems when this class changes.
     */
    public static final long serialVersionUID = -1L;


    ////////////////////////////////////////////////////////////////////////////
    // Public
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Returns a human readable string.
     */
    @Override
    public final String toString() {
        StringBuffer sb = new StringBuffer();

        contentToString( sb );

        String name = getClass().getName();
        name = name.substring( name.lastIndexOf( '.' ) + 1 );

        return name + '{' + sb.toString() + '}';
    }


    ////////////////////////////////////////////////////////////////////////////
    // Protected
    ////////////////////////////////////////////////////////////////////////////
		
    /**
     * Constructs the content for the toString() method.
     */
    protected abstract void contentToString(StringBuffer sb);


    /**
     * Constructs the content for the toString() method.
     */
    protected final void append(StringBuffer sb, String name, byte[] val) {
        if (val != null) {
            if (sb.length() != 0) {
                sb.append( ", " );
            }

            sb.append( name ).append( "=byte[" ).append( val.length ).append( ']' );
        }
    }


    /**
     * Constructs the content for the toString() method.
     */
    protected final void append(StringBuffer sb, String name, Object val) {
        if (val != null) {
            if (sb.length() != 0) {
                sb.append( ", " );
            }

            sb.append( name ).append( '=' ).append( val );
        }
    }

}

