       IDENTIFICATION DIVISION.

       PROGRAM-ID.    PRUERR.
       AUTHOR.        CVJ.
       SECURITY.      MENSAJES DE ERRORES EN LOS FICHEROS.

       ENVIRONMENT DIVISION.

       INPUT-OUTPUT SECTION.
       FILE-CONTROL.


       DATA DIVISION.
       FILE SECTION.

       WORKING-STORAGE SECTION.

       LINKAGE SECTION.

       77  LK-ERRORES    PIC XX.
       77  NOMBREFICHERO PIC X(256).
       77  PROG          PIC X(128).

       PROCEDURE DIVISION USING
                 LK-ERRORES
                 NOMBREFICHERO
                 PROG.

       PROGRAMA SECTION.
       INICIO.
            DISPLAY "ERROR:"
                    LK-ERRORES ", FICHERO:"
                    NOMBREFICHERO ", PROGRAMA:"
                    PROG UPON SYSOUT.
       FIN-PROCESO.
           STOP RUN.  *>Acabando el proceso por error en cobol que no ha sido capturado.

