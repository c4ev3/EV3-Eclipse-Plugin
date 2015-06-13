/*
 \file		$(baseName).c
 \author	${user}
 \date		${date}
 \brief		Simple Hello World! for the Ev3
*/

#include <ev3.h>

int main(void)
{
	InitEV3();

	//TODO Place here your variables


	//TODO Place here your code
	LcdPrintf(1, "Hello World!\n");
	Wait(SEC_2);

	FreeEV3();
	return 0;
}
