      ******************************************************************
      * Author:
      * Date:
      * Purpose:
      * Tectonics: cobc
      ******************************************************************
       IDENTIFICATION DIVISION.
       PROGRAM-ID. PRUEBA.
       DATA DIVISION.
       FILE SECTION.
       WORKING-STORAGE SECTION.
       01 NOMBRE PIC X(256).
       PROCEDURE DIVISION.
       MAIN-PROCEDURE.
            MOVE "/home/carlos/ALFA/MENU/SG/FSGNODO" TO NOMBRE.
            DISPLAY "CALLING ASGNODO USING " NOMBRE UPON SYSOUT.
            CALL "ASGNODO" USING NOMBRE.
            STOP RUN.
       

