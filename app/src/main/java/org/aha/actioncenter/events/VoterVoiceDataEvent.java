package org.aha.actioncenter.events;

/**
 * Created by markusmcgee on 4/29/15.
 */
public class VoterVoiceDataEvent extends Event {

    public static final String VOTER_VOICE_CREATE_DATA = "VOTER_VOICE_CREATE_DATA";
    public static final String VOTER_VOICE_GET_CAMPAIGN_DATA = "VOTER_VOICE_GET_CAMPAIGN_DATA";
    public static final String VOTER_VOICE_GET_CAMPAIGN_LIST_DATA = "VOTER_VOICE_GET_CAMPAIGN_LIST_DATA";
    public static final String VOTER_VOICE_POST_DATA = "VOTER_VOICE_POST_DATA";
    public static final String VOTER_VOICE_PROFILE_DATA = "VOTER_VOICE_PROFILE_DATA";
    public static final String VOTER_VOICE_GET_TARGETED_MESSAGE_DATA = "VOTER_VOICE_GET_TARGETED_MESSAGE_DATA";
    public static final String VOTER_VOICE_GET_MATCHES_FOR_CAMPAIGN_DATA = "VOTER_VOICE_GET_MATCHES_FOR_CAMPAIGN_DATA";

    private boolean mSuccess = false;

    public VoterVoiceDataEvent(String tagName){
        this.tagName = tagName;
    }

    public VoterVoiceDataEvent(String className, String message) {
        this.message = message; this.className = className;
    }

    public boolean isSuccces(){
        return mSuccess;
    }

    public void setSuccess(boolean success) {
        mSuccess = success;
    }
}
