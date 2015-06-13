//============================================================================
// Name        : $(baseName).cpp
// Author      : $(author)
// Version     :
// Copyright   : $(copyright)
// Description : Hello World in C++
//============================================================================

#include <ev3.h>

int main()
{
  InitEV3();

  std::string greeting("Hello World!\n");

  LcdPrintf(1, greeting.c_str());
  Wait(SEC_2);

  FreeEV3();
}
