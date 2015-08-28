//============================================================================
// Name        : $(baseName).cpp
// Author      : $(author)
// Version     :
// Copyright   : $(copyright)
// Description : Hello World in C++
//============================================================================

#include <ev3.h>
#include <string>

int main()
{
  InitEV3();

  std::string greeting("Hello World!");

  LcdPrintf(1, "%s\n", greeting.c_str());
  Wait(2000);

  FreeEV3();
}
