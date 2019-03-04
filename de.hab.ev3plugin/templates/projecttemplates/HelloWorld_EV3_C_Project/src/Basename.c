/*
 \file		$(baseName).c
 \author	${user}
 \date		${date}
 \brief		Simple Hello World! for the Ev3
*/

#include <ev3.h>

int main(void)
{
	
	TermPrintf("Hello World!\n");
	Wait(SEC_2);

	return 0;
}
