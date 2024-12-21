package com.spacECE.spaceceedu.space_active;

import android.util.Log;

public class space_active_data_holder_all_in_one {
    String activity_no;
    String activity_name;
    String activity_level;
    String activity_dev_domain;
    String activity_objectives;
    String activity_key_dev;
    String activity_material;
    String activity_assessment;
    String activity_process;
    String activity_instructions;
    String activity_complete_status="-1";
    String activity_image="null";
    String activity_video="null";
    String activity_type_status;
    String activity_date;
    String activity_playlist_id;
    String getActivity_playlist_description;
    String getActivity_playlist_name;

    public space_active_data_holder_all_in_one(String activity_assessment, String activity_date, String activity_dev_domain, String activity_instructions, String activity_key_dev, String activity_level, String activity_material, String activity_name, String activity_no, String activity_objectives, String activity_playlist_id, String activity_process, String activity_type_status, String getActivity_playlist_description, String getActivity_playlist_name) {
        this.activity_assessment = activity_assessment;
        this.activity_date = activity_date;
        this.activity_dev_domain = activity_dev_domain;
        this.activity_instructions = activity_instructions;
        this.activity_key_dev = activity_key_dev;
        this.activity_level = activity_level;
        this.activity_material = activity_material;
        this.activity_name = activity_name;
        this.activity_no = activity_no;
        this.activity_objectives = activity_objectives;
        this.activity_playlist_id = activity_playlist_id;
        this.activity_process = activity_process;
        this.activity_type_status = activity_type_status;
        this.getActivity_playlist_description = getActivity_playlist_description;
        this.getActivity_playlist_name = getActivity_playlist_name;
    }

    public String getActivity_image() {
        return activity_image;
    }

    public void setActivity_image(String activity_image) {
        this.activity_image = activity_image;
    }

    public String getActivity_complete_status() {
        return activity_complete_status;
    }

    public void setActivity_complete_status(String activity_complete_status) {
        this.activity_complete_status = activity_complete_status;
    }

    public String getActivity_video() {
        return activity_video;
    }

    public void setActivity_video(String activity_video) {
        this.activity_video = activity_video;
    }

    public void print_All(){
        Log.e( "print_All: ",activity_no+"==no\n"+activity_name+"==name\n"+activity_level+"==level\n"+activity_dev_domain+"==dev_domain\n"+activity_objectives+"==objectives\n"+activity_key_dev+"==key_dev\n"+activity_material+"==material\n"+activity_assessment+"==assessment\n"+activity_process+"==process\n"+activity_instructions+"==instructions\n"+activity_complete_status+"==complete_status\n"+activity_image+"==image\n"+activity_type_status+"==type_status\n"+activity_date+"==date\n"+activity_playlist_id+"==playlist_id\n"+getActivity_playlist_description+"==playlist_description\n"+getActivity_playlist_name+"==playlist_name\n"+activity_video+"==video\n");
    }
}
