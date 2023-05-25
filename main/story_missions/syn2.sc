MISSION_START
// *****************************************************************************************
// *****************************************************************************************
// *****************************************************************************************
// ************************************** DRIVER 2 *****************************************
// ********************************* Mission Description ***********************************
// *****************************************************************************************
// This was made as a test bed for trying out sniping in the mission. Ignore it.
// *****************************************************************************************

SCRIPT_NAME driv2

// Mission start stuff

GOSUB mission_start_driv2

IF HAS_DEATHARREST_BEEN_EXECUTED
	GOSUB mission_driv2_failed
ENDIF

GOSUB mission_cleanup_driv2

MISSION_END

{
 
// Variables for mission

LVAR_INT d2_ambushed_van d2_player_bike d2_last_player_car_before_meet
LVAR_INT d2_num_of_packages d2_packages[4] //d2_num_packages_collected // global (temp)
LVAR_FLOAT d2_package_x[4] d2_package_y[4] d2_package_z[4]
LVAR_INT d2_gang_bikers[4]
LVAR_INT d2_gang_bikes[4]
LVAR_FLOAT d2_bike_speed[4]
LVAR_INT d2_bike_health[4] d2_last_bike_health[4] d2_damage_done

LVAR_FLOAT d2_city_centre_x d2_city_centre_y d2_city_centre_z
LVAR_FLOAT d2_bike_first_pt_x[4] d2_bike_first_pt_y[4] d2_bike_first_pt_z[4]
LVAR_FLOAT d2_bike_dest_x d2_bike_dest_y d2_bike_dest_z

LVAR_FLOAT d2_player_dest_x d2_player_dest_y d2_player_dest_z

LVAR_FLOAT d2_player_x d2_player_y d2_player_z d2_dist_from_player_to_bike
LVAR_FLOAT d2_bike_x d2_bike_y d2_bike_z
LVAR_FLOAT d2_player_heading

LVAR_FLOAT d2_player_offset_x1 d2_player_offset_y1 d2_player_offset_x2 d2_player_offset_y2 d2_dummy_z

LVAR_INT d2_task_status d2_event_type

LVAR_INT d2_biker_task_status[4] d2_biker_relationship_set[4]

LVAR_INT d2_index d2_package_at_dest_index

LVAR_INT d2_current_area_visible

// flags

LVAR_INT d2_gang_created
LVAR_INT d2_package_collected[4]
//LVAR_INT d2_player_bike_blip_removed
LVAR_INT /*d2_player_picked_up_bike*/ d2_player_reached_deal_location
LVAR_INT d2_player_sniped_at_biker d2_projectile_reached_area
LVAR_INT d2_cur_bike_state[4] d2_not_started_chase d2_wandering_randomly d2_going_to_city_centre d2_going_to_first_point d2_going_to_destination
LVAR_INT d2_getting_back_on_bike[4] d2_biker_picking_up_dropped_package[4]
LVAR_INT d2_biker_attacking_player[4] d2_biker_shooting_at_player[4]
LVAR_INT d2_bikes_going_to_destination d2_any_package_at_destination
LVAR_INT d2_any_packages_in_water d2_any_packages_been_destroyed
LVAR_INT d2_package_attach_state[4] d2_not_attached d2_attached_to_bike d2_attached_to_char
LVAR_INT d2_all_packages_collected

LVAR_INT d2_leftshoulder1_pressed_last_frame d2_in_range_last_printed

LVAR_INT d2_cutscene_skipped d2_player_given_mission_brief
LVAR_INT d2_first_help_text_cleared d2_second_help_text_cleared d2_player_got_within_10m_of_snatchable_package

LVAR_INT d2_player_entered_any_car

LVAR_INT d2_package_immovable[4]

LVAR_INT d2_fake_creates

// blips

LVAR_INT d2_package_blips[4] /*d2_player_bike_blip*/ d2_ambushed_van_blip d2_player_dest_blip

// **************************************** Mission Start **********************************

mission_start_driv2:

REGISTER_MISSION_GIVEN

LOAD_MISSION_TEXT FARLIE2

flag_player_on_mission = 1

WAIT 0

LVAR_INT d2_gang_biker_decisions
		COPY_CHAR_DECISION_MAKER -1 d2_gang_biker_decisions

// ************************************* Initialise variables *****************************
VAR_INT d2_num_packages_collected
d2_num_of_packages = 4
d2_num_packages_collected = 0

d2_index = 0
WHILE d2_index < d2_num_of_packages
	d2_package_collected[d2_index] = 0
	d2_package_immovable[d2_index] = 0
	d2_cur_bike_state[d2_index] = 0
	d2_getting_back_on_bike[d2_index] = 0
	d2_biker_picking_up_dropped_package[d2_index] = 0
	d2_biker_attacking_player[d2_index] = 0
	d2_biker_shooting_at_player[d2_index] = 0
	d2_bike_health[d2_index] = 1000
	d2_last_bike_health[d2_index] = 1000
	d2_package_attach_state[d2_index] = 0
	d2_bike_speed[d2_index] = 20.0
	d2_biker_relationship_set[d2_index] = 0
	d2_index++
ENDWHILE

d2_bike_dest_x = -2730.14
d2_bike_dest_y = 84.24
d2_bike_dest_z = 3.04

d2_bike_first_pt_x[0] = -2600.53
d2_bike_first_pt_y[0] = 1337.91
d2_bike_first_pt_z[0] = 5.60
d2_bike_first_pt_x[1] = -2855.52
d2_bike_first_pt_y[1] = 719.95
d2_bike_first_pt_z[1] = 26.84
d2_bike_first_pt_x[2] = -1897.94
d2_bike_first_pt_y[2] = -576.43
d2_bike_first_pt_z[2] = 22.99
d2_bike_first_pt_x[3] = -2811.49
d2_bike_first_pt_y[3] = -326.14
d2_bike_first_pt_z[3] = 5.72

//d2_player_dest_x = -2622.34
//d2_player_dest_y = 1404.97
//d2_player_dest_z = 6.15
d2_player_dest_x = -2622.49
d2_player_dest_y = 1406.6
d2_player_dest_z = 6.15

d2_gang_created = 0

//d2_player_bike_blip_removed = 0
//d2_player_picked_up_bike = 0
d2_player_reached_deal_location = 0
d2_player_sniped_at_biker = 0
d2_projectile_reached_area = 0

// enum bike states
d2_not_started_chase = 0
d2_wandering_randomly = 1
d2_going_to_city_centre = 2
d2_going_to_first_point = 3
d2_going_to_destination = 4

// enum package states
d2_not_attached = 0
d2_attached_to_bike = 1
d2_attached_to_char = 2

d2_bikes_going_to_destination = 0
d2_any_package_at_destination = 0
d2_any_packages_in_water = 0
d2_any_packages_been_destroyed = 0

d2_all_packages_collected = 0

d2_city_centre_x = -2143.27
d2_city_centre_y = 918.38
d2_city_centre_z = 79.42

d2_leftshoulder1_pressed_last_frame = 0
d2_in_range_last_printed = 0

d2_first_help_text_cleared = 0
d2_second_help_text_cleared = 0
d2_player_got_within_10m_of_snatchable_package = 0

d2_player_entered_any_car = 0

d2_fake_creates = 0

// ***************************************START OF CUTSCENE********************************

SET_AREA_VISIBLE 0

// ****************************************END OF CUTSCENE*********************************

REQUEST_MODEL FCR900
REQUEST_MODEL BOXVILLE
REQUEST_MODEL kmb_packet
REQUEST_MODEL WMYCR
REQUEST_MODEL HMYCR
REQUEST_MODEL MICRO_UZI
WHILE NOT HAS_MODEL_LOADED FCR900
   OR NOT HAS_MODEL_LOADED BOXVILLE
   OR NOT HAS_MODEL_LOADED kmb_packet
   OR NOT HAS_MODEL_LOADED WMYCR
   OR NOT HAS_MODEL_LOADED HMYCR
   OR NOT HAS_MODEL_LOADED MICRO_UZI
	WAIT 0
ENDWHILE
REQUEST_ANIMATION BIKES
REQUEST_ANIMATION MISC
WHILE NOT HAS_ANIMATION_LOADED BIKES
   OR NOT HAS_ANIMATION_LOADED MISC
	WAIT 0
ENDWHILE

//load weapons to give player
REQUEST_MODEL DESERT_EAGLE
REQUEST_MODEL MP5LNG
REQUEST_MODEL CUNTGUN
REQUEST_MODEL MINIGUN
REQUEST_MODEL M4

LOAD_ALL_MODELS_NOW

GIVE_WEAPON_TO_CHAR scplayer WEAPONTYPE_DESERT_EAGLE 30000
GIVE_WEAPON_TO_CHAR scplayer WEAPONTYPE_MP5 30000
GIVE_WEAPON_TO_CHAR scplayer WEAPONTYPE_COUNTRYRIFLE 30000
GIVE_WEAPON_TO_CHAR scplayer WEAPONTYPE_MINIGUN 30000
GIVE_WEAPON_TO_CHAR scplayer WEAPONTYPE_M4 30000

LOAD_SCENE -1917.57242 388.6330 30.0

SET_CHAR_COORDINATES scplayer -1888.89 366.99 44.40
SET_CHAR_HEADING scplayer 274.0

SET_CAMERA_BEHIND_PLAYER
RESTORE_CAMERA_JUMPCUT

// fades the screen in 

SET_FADING_COLOUR 0 0 0

SWITCH_WIDESCREEN FALSE

WAIT 500

DO_FADE 1500 FADE_IN
WHILE GET_FADING_STATUS
    WAIT 0
ENDWHILE

SET_PLAYER_CONTROL player1 ON

SET_MAX_WANTED_LEVEL 0
CLEAR_WANTED_LEVEL player1

// CREATE statements to keep the compiler happy

IF d2_fake_creates = 1
	CREATE_CAR BOXVILLE 0.0 0.0 -100.0 d2_ambushed_van
	ADD_BLIP_FOR_CAR d2_ambushed_van d2_ambushed_van_blip
	d2_index = 0
	WHILE d2_index < d2_num_of_packages
		CREATE_OBJECT kmb_packet 0.0 0.0 -100.0 d2_packages[d2_index]
		ADD_BLIP_FOR_OBJECT d2_packages[d2_index] d2_package_blips[d2_index]
		CREATE_CHAR PEDTYPE_MISSION1 WMYCR 0.0 0.0 -100.0 d2_gang_bikers[d2_index]
		CREATE_CAR FCR900 0.0 0.0 -100.0 d2_gang_bikes[d2_index]
		d2_index++
	ENDWHILE
	CREATE_CAR FCR900 0.0 0.0 -100.0 d2_player_bike
	//ADD_BLIP_FOR_CAR d2_player_bike d2_player_bike_blip
ENDIF

//// stop bikes getting stuck on winding road
//SWITCH_ROADS_OFF -2130.87 903.39 45.48 -2013.14 959.18 80.03
// stop traffic jams at location of ambush
//SWITCH_ROADS_OFF -1859.24 349.43 13.18 -1792.44 418.63 21.34
SWITCH_ROADS_OFF -2014.82 181.4 -13.0 -1614.82 581.4 47.0

ADD_BLIP_FOR_COORD -1814.82 381.40 17.09 d2_ambushed_van_blip
SET_COORD_BLIP_APPEARANCE d2_ambushed_van_blip 0

PRINT_NOW ( DRV3_11 ) 10000 0

// Mission loop
driv2_loop:

WAIT 0

	//IF d2_player_picked_up_bike = 1
	IF NOT d2_player_reached_deal_location = 1

		// get handle for last car player is in before reaching meet
		IF IS_CHAR_IN_ANY_CAR scplayer
			STORE_CAR_CHAR_IS_IN scplayer d2_last_player_car_before_meet
			IF NOT d2_player_entered_any_car = 1
				d2_player_entered_any_car = 1
			ENDIF
		ENDIF

		IF NOT d2_gang_created = 1
		AND LOCATE_CHAR_ANY_MEANS_2D scplayer -1814.82 381.40 200.0 200.0 FALSE
			REMOVE_BLIP d2_ambushed_van_blip
			GOSUB d2_create_ambushed_van_and_gang
			d2_gang_created = 1
		ENDIF

		IF d2_gang_created = 1

			GET_AREA_VISIBLE d2_current_area_visible
			IF d2_current_area_visible = 0
				d2_index = 0
				WHILE d2_index < d2_num_of_packages
				AND NOT d2_player_sniped_at_biker = 1
					IF NOT IS_CHAR_DEAD d2_gang_bikers[d2_index]
						GET_CHAR_HIGHEST_PRIORITY_EVENT d2_gang_bikers[d2_index] d2_event_type
						IF d2_event_type = EVENT_SHOT_FIRED_WHIZZED_BY
							d2_player_sniped_at_biker = 1
                            PRINT_NOW TEST1 2000 0
						ENDIF
					ENDIF
					d2_index++
				ENDWHILE
				IF NOT d2_player_sniped_at_biker = 1
					IF NOT d2_projectile_reached_area = 1
					AND IS_PROJECTILE_IN_AREA -1824.82 371.40 12.09 -1804.82 391.40 22.09
						d2_projectile_reached_area = 1
						TIMERA = 0
					ENDIF
					IF d2_projectile_reached_area = 1
						d2_index = 0
						WHILE d2_index < d2_num_of_packages
							IF NOT IS_CHAR_DEAD d2_gang_bikers[d2_index]
								LVAR_FLOAT d2_xmin d2_xmax d2_ymin d2_ymax d2_zmin d2_zmax
								GET_CHAR_COORDINATES d2_gang_bikers[d2_index] player_x player_y player_z
								d2_xmin = player_x - 3.0
								d2_xmax = player_x + 3.0
								d2_ymin = player_y - 3.0
								d2_ymax = player_y + 3.0
								d2_zmin = player_z - 3.0
								d2_zmax = player_z + 3.0
								IF IS_EXPLOSION_IN_AREA EXPLOSION_GRENADE d2_xmin d2_ymin d2_zmin d2_xmax d2_ymax d2_zmax
								OR IS_EXPLOSION_IN_AREA EXPLOSION_MOLOTOV d2_xmin d2_ymin d2_zmin d2_xmax d2_ymax d2_zmax
								OR IS_EXPLOSION_IN_AREA EXPLOSION_ROCKET d2_xmin d2_ymin d2_zmin d2_xmax d2_ymax d2_zmax
								OR IS_EXPLOSION_IN_AREA EXPLOSION_ROCKET_WEAK d2_xmin d2_ymin d2_zmin d2_xmax d2_ymax d2_zmax
								OR IS_EXPLOSION_IN_AREA EXPLOSION_SMALL d2_xmin d2_ymin d2_zmin d2_xmax d2_ymax d2_zmax
								OR IS_EXPLOSION_IN_AREA EXPLOSION_TINY d2_xmin d2_ymin d2_zmin d2_xmax d2_ymax d2_zmax
									PRINT_NOW ( DRV3_9 ) 5000 0
                                    PRINT_BIG M_FAIL 5000 1
                                    SET_PLAYER_CONTROL player1 FALSE
									WAIT 2000
									DO_FADE 1000 FADE_OUT
									WHILE GET_FADING_STATUS
										WAIT 0
									ENDWHILE
									gosub reset_test
									CLEAR_THIS_BIG_PRINT M_FAIL
									CLEAR_PRINTS
									DO_FADE 1000 FADE_IN
									GOTO driv2_loop
								ENDIF
							ENDIF
							d2_index++
						ENDWHILE
						IF TIMERA > 2000
							d2_player_sniped_at_biker = 1
                            PRINT_NOW TEST2 2000 0
						ENDIF
					ENDIF
				ENDIF
			ENDIF

			

			IF LOCATE_CHAR_ANY_MEANS_2D scplayer -1814.82 381.40 50.0 50.0 FALSE
                PRINT_BIG TEST3 100 4
            ENDIF
            IF d2_player_sniped_at_biker = 1
				GOSUB d2_play_cutscene_for_deal
			ENDIF

		ENDIF

	ENDIF

GOTO driv2_loop


// ********************************** Mission GOSUBS ************************************

reset_test:
gosub d2_destroy_ambushed_van_and_gang
CLEAR_PRINTS
d2_gang_created = 0
d2_player_sniped_at_biker = 0
d2_projectile_reached_area = 0
SWITCH_WIDESCREEN OFF
SET_CAMERA_BEHIND_PLAYER
RESTORE_CAMERA_JUMPCUT
SET_PLAYER_CONTROL player1 ON
RETURN

// *****************************************************************
// 							 Deal cutscene
// *****************************************************************

	d2_play_cutscene_for_deal:

		CLEAR_AREA -1814.82 381.40 17.09 100.0 FALSE

		SET_PLAYER_CONTROL player1 OFF
		SWITCH_WIDESCREEN ON

		SET_FIXED_CAMERA_POSITION -1808.39 374.18 17.98 0.0 0.0 0.0
		POINT_CAMERA_AT_POINT -1813.41 376.31 17.32 JUMP_CUT

		WAIT 2000

		gosub d2_destroy_ambushed_van_and_gang

		CLEAR_PRINTS

		d2_gang_created = 0
		d2_player_sniped_at_biker = 0
		d2_projectile_reached_area = 0
		SWITCH_WIDESCREEN OFF
		SET_CAMERA_BEHIND_PLAYER
		RESTORE_CAMERA_JUMPCUT
		SET_PLAYER_CONTROL player1 ON

	RETURN

// ************************************************************
// 						 Destroy gang setup
// ************************************************************

d2_destroy_ambushed_van_and_gang:
IF DOES_VEHICLE_EXIST d2_ambushed_van
	DELETE_CAR d2_ambushed_van
ENDIF
d2_index = 0
WHILE d2_index < d2_num_of_packages
	IF DOES_OBJECT_EXIST d2_packages[d2_index]
		DELETE_OBJECT d2_packages[d2_index]
	ENDIF
	IF DOES_CHAR_EXIST d2_gang_bikers[d2_index]
		DELETE_CHAR d2_gang_bikers[d2_index]
	ENDIF
	IF DOES_VEHICLE_EXIST d2_gang_bikes[d2_index]
		DELETE_CAR d2_gang_bikes[d2_index]
	ENDIF
	d2_index++
ENDWHILE
IF DOES_VEHICLE_EXIST d2_player_bike
	DELETE_CAR d2_player_bike
ENDIF
RETURN

// ************************************************************
// 						 Create gang setup
// ************************************************************

	d2_create_ambushed_van_and_gang:

		CLEAR_AREA -1814.82 381.40 17.09 100.0 TRUE
		
		SET_CAR_MODEL_COMPONENTS BOXVILLE 0 0
		CREATE_CAR BOXVILLE -1814.82 381.40 9.96 d2_ambushed_van
		SET_CAR_HEADING d2_ambushed_van 33.27
		POP_CAR_DOOR d2_ambushed_van REAR_LEFT_DOOR FALSE
		OPEN_CAR_DOOR d2_ambushed_van REAR_RIGHT_DOOR
		// make sure can't destroy ambushed van before the cutscene's played
		SET_CAR_PROOFS d2_ambushed_van TRUE TRUE TRUE TRUE TRUE
		ADD_BLIP_FOR_CAR d2_ambushed_van d2_ambushed_van_blip
		SET_BLIP_AS_FRIENDLY d2_ambushed_van_blip TRUE

		CREATE_CAR FCR900 -1814.86 376.0 15.68 d2_gang_bikes[0]
		SET_CAR_HEADING d2_gang_bikes[0] 26.65
		CREATE_CAR FCR900 -1817.44 380.33 15.68 d2_gang_bikes[1]
		SET_CAR_HEADING d2_gang_bikes[1] 23.76
		CREATE_CAR FCR900 -1818.09 377.08 15.68 d2_gang_bikes[2]
		SET_CAR_HEADING d2_gang_bikes[2] 32.17
		CREATE_CAR FCR900 -1816.66 374.73 15.68 d2_gang_bikes[3]
		SET_CAR_HEADING d2_gang_bikes[3] 30.21
		d2_index = 0
		WHILE d2_index < d2_num_of_packages
			SET_CAR_ONLY_DAMAGED_BY_PLAYER d2_gang_bikes[d2_index] TRUE
			SET_CAN_BURST_CAR_TYRES d2_gang_bikes[d2_index] FALSE
			FREEZE_CAR_POSITION d2_gang_bikes[d2_index] TRUE
			d2_index++
		ENDWHILE
		CREATE_CAR FCR900 -1819.01 386.28 16.58 d2_player_bike
		SET_CAR_HEADING d2_player_bike 49.56
		SET_CAR_ONLY_DAMAGED_BY_PLAYER d2_player_bike TRUE
		SET_CAN_BURST_CAR_TYRES d2_player_bike FALSE

		CREATE_CHAR PEDTYPE_MISSION1 WMYCR -1811.85 375.88 15.68 d2_gang_bikers[0]
		SET_CHAR_HEADING d2_gang_bikers[0] 27.32
		CREATE_CHAR_INSIDE_CAR d2_gang_bikes[1] PEDTYPE_MISSION1 WMYCR d2_gang_bikers[1]
		CREATE_CHAR_INSIDE_CAR d2_gang_bikes[2] PEDTYPE_MISSION1 HMYCR d2_gang_bikers[2]
		CREATE_CHAR_INSIDE_CAR d2_gang_bikes[3] PEDTYPE_MISSION1 HMYCR d2_gang_bikers[3]

		ADD_CHAR_DECISION_MAKER_EVENT_RESPONSE d2_gang_biker_decisions EVENT_DAMAGE TASK_SIMPLE_BE_DAMAGED 100.0 100.0 100.0 100.0 TRUE FALSE
		d2_index = 0
		WHILE d2_index < d2_num_of_packages
			IF NOT IS_CHAR_DEAD d2_gang_bikers[d2_index]
				SET_CHAR_DECISION_MAKER d2_gang_bikers[d2_index] d2_gang_biker_decisions
				GIVE_WEAPON_TO_CHAR d2_gang_bikers[d2_index] WEAPONTYPE_MICRO_UZI 99999
				SET_CHAR_CAN_BE_KNOCKED_OFF_BIKE d2_gang_bikers[d2_index] 2 //KNOCKOFFBIKE_ALWAYSNORMAL
				SET_CHAR_ONLY_DAMAGED_BY_PLAYER d2_gang_bikers[d2_index] TRUE
				CLEAR_ALL_CHAR_RELATIONSHIPS d2_gang_bikers[d2_index] ACQUAINTANCE_TYPE_PED_HATE
				CLEAR_ALL_CHAR_RELATIONSHIPS d2_gang_bikers[d2_index] ACQUAINTANCE_TYPE_PED_DISLIKE
			ENDIF
			d2_index++
		ENDWHILE

		// make sure can't destroy bikes and can only snipe biker guys before the cutscene's played
		d2_index = 0
		WHILE d2_index < d2_num_of_packages
			IF NOT IS_CHAR_DEAD d2_gang_bikers[d2_index]
				IF d2_index = 0
					SET_CHAR_PROOFS d2_gang_bikers[d2_index] TRUE TRUE TRUE TRUE TRUE
					SET_CHAR_VISIBLE d2_gang_bikers[d2_index] FALSE
				ENDIF
			ENDIF
			IF NOT IS_CAR_DEAD d2_gang_bikes[d2_index]
				SET_CAR_PROOFS d2_gang_bikes[d2_index] TRUE TRUE TRUE TRUE TRUE
			ENDIF
			d2_index++
		ENDWHILE
		SET_CAR_PROOFS d2_player_bike TRUE TRUE TRUE TRUE TRUE

		CREATE_OBJECT kmb_packet -1812.30 377.57 9.34 d2_packages[0]
		d2_index = 0
		WHILE d2_index < d2_num_of_packages
			IF NOT d2_index = 0
				CREATE_OBJECT kmb_packet 0.0 0.0 -100.0 d2_packages[d2_index]
				ATTACH_OBJECT_TO_CAR d2_packages[d2_index] d2_gang_bikes[d2_index] 0.0 -0.9 0.5 0.0 0.0 0.0
				d2_package_attach_state[d2_index] = d2_attached_to_bike
			ELSE
				IF NOT IS_CHAR_DEAD d2_gang_bikers[0]
					TASK_PICK_UP_OBJECT d2_gang_bikers[0] d2_packages[0] 0.0 0.0 0.0 PED_HANDL HOLD_ORIENTATE_BONE_FULL NULL NULL FALSE
					d2_package_attach_state[0] = d2_attached_to_char
					SET_OBJECT_VISIBLE d2_packages[0] FALSE
				ENDIF
			ENDIF
			SET_OBJECT_COLLISION d2_packages[d2_index] TRUE
			SET_OBJECT_DYNAMIC d2_packages[d2_index] TRUE
			SET_OBJECT_PROOFS d2_packages[d2_index] TRUE TRUE TRUE TRUE TRUE 
			SET_OBJECT_COLLISION_DAMAGE_EFFECT d2_packages[d2_index] FALSE
			d2_index++
		ENDWHILE

	RETURN



// ******************************** Mission driv2 failed **********************************

mission_driv2_failed:
PRINT_BIG ( M_FAIL ) 5000 1 //"Mission Failed"
RETURN
		

// *********************************** Mission cleanup *************************************

mission_cleanup_driv2:

MARK_MODEL_AS_NO_LONGER_NEEDED BOXVILLE
MARK_MODEL_AS_NO_LONGER_NEEDED FCR900
MARK_MODEL_AS_NO_LONGER_NEEDED kmb_packet
MARK_MODEL_AS_NO_LONGER_NEEDED WMYCR
MARK_MODEL_AS_NO_LONGER_NEEDED HMYCR
MARK_MODEL_AS_NO_LONGER_NEEDED MICRO_UZI
REMOVE_ANIMATION BIKES
REMOVE_ANIMATION MISC
d2_index = 0
WHILE d2_index < d2_num_of_packages
	REMOVE_BLIP d2_package_blips[d2_index]
	IF DOES_OBJECT_EXIST d2_packages[d2_index]
		IF NOT IS_OBJECT_ON_SCREEN d2_packages[d2_index]
			DELETE_OBJECT d2_packages[d2_index]
		ENDIF
	ENDIF
	d2_index++
ENDWHILE
//REMOVE_BLIP d2_player_bike_blip
REMOVE_BLIP d2_ambushed_van_blip
// reset car proofs
IF NOT IS_CAR_DEAD d2_ambushed_van
	SET_CAR_PROOFS d2_ambushed_van FALSE FALSE FALSE FALSE FALSE
ENDIF
d2_index = 0
WHILE d2_index < d2_num_of_packages
	IF NOT IS_CAR_DEAD d2_gang_bikes[d2_index]
		SET_CAR_PROOFS d2_gang_bikes[d2_index] FALSE FALSE FALSE FALSE FALSE
		FREEZE_CAR_POSITION d2_gang_bikes[d2_index] FALSE
	ENDIF
	d2_index++
ENDWHILE
IF NOT IS_CAR_DEAD d2_player_bike
	SET_CAR_PROOFS d2_player_bike FALSE FALSE FALSE FALSE FALSE
ENDIF
REMOVE_DECISION_MAKER d2_gang_biker_decisions
CLEAR_HELP
//SWITCH_ROADS_BACK_TO_ORIGINAL -2130.87 903.39 45.48 -2013.14 959.18 80.03
SWITCH_ROADS_BACK_TO_ORIGINAL -2014.82 181.4 -13.0 -1614.82 581.4 47.0
IF IS_PLAYER_PLAYING player1
	HIDE_CHAR_WEAPON_FOR_SCRIPTED_CUTSCENE scplayer FALSE
ENDIF

flag_player_on_mission = 0
MISSION_HAS_FINISHED
RETURN


}
