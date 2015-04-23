/*
 ============================================================================
 Name        : $(baseName).c
 Author      : $(user)
 Version     :
 Created	 : $(date)
 Description : Sample Project with all EV3 header files included
 ============================================================================
 */


#include <stdio.h>

/****EV3 includes START****/
#include <ev3_constants.h>
#include <ev3_command.h>
#include <ev3_output.h>
#include <ev3sensor.h>
#include <ev3_button.h>
#include <ev3_lcd.h>
/*****EV3 includes END*****/


int main(void)
{
	/****Variable START****/
	//ToDo Place here your Variables


	/****Variable END****/

	/****EV3 inits START****/
	OutputInit();
	initSensors();
	ButtonLedInit();
	LcdInit();
	LcdClean();
	/*****EV3 inits END*****/

	/****CODE START****/
	//ToDo Place here your code





	/****CODE END****/

	/****EV3 close START****/
	OutputClose();
	OutputExit();
	ButtonLedClose();
	ButtonLedExit();
	LcdExit();
	/****EV3 close START****/

	return 0;
}
