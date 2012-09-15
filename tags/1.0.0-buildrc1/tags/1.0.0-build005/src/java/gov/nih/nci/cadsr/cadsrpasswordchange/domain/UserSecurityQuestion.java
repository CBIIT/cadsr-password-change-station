package gov.nih.nci.cadsr.cadsrpasswordchange.domain;

import java.sql.Date;

/**
 * This is a DTO class.
 *
 * @author generated
 */
public class UserSecurityQuestion extends AbstractDto {

    ////////////////////////////////////////////////////////////////////////////
    // Static
    ////////////////////////////////////////////////////////////////////////////

    public static final String TABLE = "User_Security_Questions";

    ////////////////////////////////////////////////////////////////////////////
    // Attributes
    ////////////////////////////////////////////////////////////////////////////

    private String uaName;
    private String question1;
    private String answer1;
    private String question2;
    private String answer2;
    private String question3;
    private String answer3;
    private Date dateModified;
    private Long attemptedCount;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new empty DTO.
     */
    public UserSecurityQuestion() {
    }

    ////////////////////////////////////////////////////////////////////////////
    // Public
    ////////////////////////////////////////////////////////////////////////////

    public String getUaName() {
        return uaName;
    }

    public void setUaName( String _val) {
        this.uaName = _val;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1( String _val) {
        this.question1 = _val;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1( String _val) {
        this.answer1 = _val;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2( String _val) {
        this.question2 = _val;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2( String _val) {
        this.answer2 = _val;
    }

    public String getQuestion3() {
        return question3;
    }

    public void setQuestion3( String _val) {
        this.question3 = _val;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3( String _val) {
        this.answer3 = _val;
    }

    public Date getDateModified() {
        return dateModified;
    }

    public void setDateModified( java.util.Date _val ) {
        setDateModified((Date)( _val != null ? new Date( _val.getTime()) : null ));
    }

    public void setDateModified( Date _val) {
        this.dateModified = _val;
    }

    public Long getAttemptedCount() {
        return attemptedCount;
    }

    public void setAttemptedCount( Long _val) {
        this.attemptedCount = _val;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * Uses 'columns' equality type.
     */
    @Override
    public boolean equals( Object _other ) {
        if (_other == this) return true;
        if (_other == null || (!(_other instanceof UserSecurityQuestion))) return false;

        UserSecurityQuestion _o = (UserSecurityQuestion) _other;

        if ( uaName == null ) {
            if ( _o.uaName != null ) return false;
        }
        else if ( _o.uaName == null || !uaName.equals( _o.uaName )) return false;

        if ( question1 == null ) {
            if ( _o.question1 != null ) return false;
        }
        else if ( _o.question1 == null || !question1.equals( _o.question1 )) return false;

        if ( answer1 == null ) {
            if ( _o.answer1 != null ) return false;
        }
        else if ( _o.answer1 == null || !answer1.equals( _o.answer1 )) return false;

        if ( question2 == null ) {
            if ( _o.question2 != null ) return false;
        }
        else if ( _o.question2 == null || !question2.equals( _o.question2 )) return false;

        if ( answer2 == null ) {
            if ( _o.answer2 != null ) return false;
        }
        else if ( _o.answer2 == null || !answer2.equals( _o.answer2 )) return false;

        if ( question3 == null ) {
            if ( _o.question3 != null ) return false;
        }
        else if ( _o.question3 == null || !question3.equals( _o.question3 )) return false;

        if ( answer3 == null ) {
            if ( _o.answer3 != null ) return false;
        }
        else if ( _o.answer3 == null || !answer3.equals( _o.answer3 )) return false;

        if ( dateModified == null ) {
            if ( _o.dateModified != null ) return false;
        }
        else if ( _o.dateModified == null || dateModified.getTime() != _o.dateModified.getTime()) return false;

        if ( attemptedCount == null ) {
            if ( _o.attemptedCount != null ) return false;
        }
        else if ( _o.attemptedCount == null || attemptedCount.longValue() != _o.attemptedCount.longValue()) return false;

        return true;
    }

    /**
     * Returns a hash code value for the object.
     */
    @Override
    public int hashCode() {
        int _ret = -1193196207; // = "UserSecurityQuestion".hashCode()
        _ret += uaName == null ? 0 : uaName.hashCode();
        _ret = 29 * _ret + (question1 == null ? 0 : question1.hashCode());
        _ret = 29 * _ret + (answer1 == null ? 0 : answer1.hashCode());
        _ret = 29 * _ret + (question2 == null ? 0 : question2.hashCode());
        _ret = 29 * _ret + (answer2 == null ? 0 : answer2.hashCode());
        _ret = 29 * _ret + (question3 == null ? 0 : question3.hashCode());
        _ret = 29 * _ret + (answer3 == null ? 0 : answer3.hashCode());
        _ret = 29 * _ret + (dateModified == null ? 0 : (int)dateModified.getTime());
        _ret = 29 * _ret + (attemptedCount == null ? 0 : (int)(attemptedCount ^ (attemptedCount >>> 32)));

        return _ret;
    }


    ////////////////////////////////////////////////////////////////////////////
    // Protected
    ////////////////////////////////////////////////////////////////////////////
		
    /**
     * Constructs the content for the toString() method.
     */
    protected void contentToString(StringBuffer sb) {
        append( sb, "uaName", uaName );
        append( sb, "question1", question1 );
        append( sb, "answer1", answer1 );
        append( sb, "question2", question2 );
        append( sb, "answer2", answer2 );
        append( sb, "question3", question3 );
        append( sb, "answer3", answer3 );
        append( sb, "dateModified", dateModified );
        append( sb, "attemptedCount", attemptedCount );
    }
}
