/*
 \file		$(baseName).c
 \author	${user}
 \date		${date}
 \brief		Simple Hello World! for the Ev3
*/

#include <ev3.h>

int main(void)
{

	// INFO This code template works only with recent versions of the API. If TermPrintln is not found,
	//      please update the API or use the "Hello World EV3 Project Old API" as template.


	//TODO Place here your variables


	//TODO Place here your code
	TermPrintln("Hello World!");
	TermPrintf("Press ENTER to exit");
	ButtonWaitForPress(BUTTON_ID_ENTER);

	return 0;
}
