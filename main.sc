// *****************************************************************************************
// *****************************************************************************************
// *****************************************************************************************
// ****************************************PC SA Main Script********************************
// *****************************************************************************************
// *****************************************************************************************
// *****************************************************************************************
                                                                                   
SCRIPT_NAME MAIN  //NEW MAIN

// ***************************************SETUP*********************************************
// *****************************************************************************************

DO_FADE 0 FADE_OUT

SET_TOTAL_NUMBER_OF_MISSIONS 0
SET_PROGRESS_TOTAL 0
SET_MISSION_RESPECT_TOTAL 0
SET_MAX_WANTED_LEVEL 6                                                 
SET_DEATHARREST_STATE OFF
SET_TIME_OF_DAY 08 00 

// *****************************************CREATE PLAYER***********************************   

VAR_INT player1 scplayer

REQUEST_COLLISION 2488.5623 -1666.8645
LOAD_SCENE 2488.5623 -1666.8645 13.3757 //LA

// *****************************************SET UP STATS************************************

SET_FLOAT_STAT ENERGY 800.0
SET_FLOAT_STAT BODY_MUSCLE 50.0
SET_FLOAT_STAT FAT 200.0 
SET_FLOAT_STAT DRIVING_SKILL 0.0
SET_INT_STAT CITIES_PASSED 0
SET_INT_STAT RESPECT 0

CREATE_PLAYER 0 2488.5623 -1666.8645 12.8757 player1 //LA hub

DISPLAY_TIMER_BARS FALSE

GET_PLAYER_CHAR player1 scplayer                                

SET_CAMERA_BEHIND_PLAYER
SET_CHAR_HEADING scplayer 262.0

LOAD_AND_LAUNCH_MISSION initial.sc
wait 0

// Global variables for missions

DECLARE_MISSION_FLAG flag_player_on_mission

VAR_INT flag_player_on_mission

SWITCH_ROADS_OFF 2500.0 -1677.0 20.0 2430.0 -1653.0 0.0     //REMOVE (SPEAK TO JOHN)

REQUEST_IPL CRACK  //REQUEST THE CRACK LAB

SET_VISIBILITY_OF_CLOSEST_OBJECT_OF_TYPE -2166.86 -236.50 40.86 40.0 crackfact_SFS TRUE

SET_VISIBILITY_OF_CLOSEST_OBJECT_OF_TYPE -2185.49 -215.55 34.31 40.0 CF_ext_dem_SFS FALSE
SET_VISIBILITY_OF_CLOSEST_OBJECT_OF_TYPE -2166.86 -236.50 40.86 40.0 LODcrackfact_SFS TRUE
SET_VISIBILITY_OF_CLOSEST_OBJECT_OF_TYPE -2185.49 -215.55 34.31 40.0 LODext_dem_SFS FALSE

DO_FADE 0 FADE_OUT
DISPLAY_ZONE_NAMES FALSE
WAIT 0
WAIT 0

SWITCH_WORLD_PROCESSING OFF //TEST!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 
SET_FADING_COLOUR 0 0 0
WAIT 2000 //Wait until keys are initialised    //TEST!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
SWITCH_WORLD_PROCESSING ON //TEST!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

SET_MAX_WANTED_LEVEL 4    

IF IS_PLAYER_PLAYING player1

    FORCE_WEATHER_NOW 1    

    GIVE_PLAYER_CLOTHES_OUTSIDE_SHOP player1 vest vest 0
    GIVE_PLAYER_CLOTHES_OUTSIDE_SHOP player1 jeansdenim jeans 2
    GIVE_PLAYER_CLOTHES_OUTSIDE_SHOP player1 sneakerbincblk sneaker 3
    GIVE_PLAYER_CLOTHES_OUTSIDE_SHOP player1 player_face head 1

    BUILD_PLAYER_MODEL player1
    STORE_CLOTHES_STATE

    //LAUNCH_MISSION debug.sc //FOR DEBUG BUILD!!!!!!
    //LAUNCH_MISSION designtools.sc //FOR  DEBUG BUILD!!!!!!!!
    SET_FADING_COLOUR 0 0  0  //FOR FINAL BUILD!!!!!!!!!!!!!!!!!
    DO_FADE 0 FADE_OUT

    flag_player_on_mission = 1
    LOAD_AND_LAUNCH_MISSION syn2.sc

    //SET_CLOSEST_ENTRY_EXIT_FLAG 2137.9055 1600.5658 10.0 ENTRYEXITS_FLAG_ENABLED FALSE //MAFIA CASINO
    IF IS_PLAYER_PLAYING player1
        SET_AREA_VISIBLE 0
        SET_PLAYER_CONTROL player1 on
    ENDIF

    RELEASE_WEATHER

    GOTO mission_start      
ENDIF



{


mission_start:

WAIT 0

IF IS_PLAYER_PLAYING player1
    GET_AREA_VISIBLE main_visible_area
    GET_INT_STAT CITIES_PASSED Return_cities_passed
    GET_CURRENT_DAY_OF_WEEK    weekday
    GET_CURRENT_LANGUAGE current_Language
    GET_CITY_PLAYER_IS_IN Player1 im_players_city
    GET_GAME_TIMER game_timer_wil  

    IF flag_player_on_mission = 0
        flag_player_on_mission = 1
        DO_FADE 250 FADE_OUT
        LOAD_AND_LAUNCH_MISSION syn2.sc
    ENDIF
ENDIF //IF IS_PLAYER_PLAYING player1

GOTO mission_start

}
