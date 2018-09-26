       IDENTIFICATION DIVISION.

       PROGRAM-ID.    PRURM.
       AUTHOR.        carlos.
       SECURITY.      BORRAR UN FICHERO (incluido su .idx)
********************* Necesario si al llamar al asignador hay un error 9A. 
********************* Este error da, en el asignador, porque se ha cambiado la estructura de la FD.
********************* Pero parece que el CALL "CBL_DELETE_FILE" NO FUNCIONA.
********************* Así que lo mejor es usar borrarArchivo de la SesionCobol.

       ENVIRONMENT DIVISION.
       DATA DIVISION.
       WORKING-STORAGE SECTION.

           COPY "WO-ERRORES".

       77  PROG     PIC X(10) VALUE "PRURM".
       77  II       PIC 99.
       77  JJ       PIC 99.
       01  VALORDIR          PIC X(128).
       01  NOMBRDIR          PIC X(128).

       01  BUFER.
           03 BUF        PIC X      OCCURS 256.

       77  ESTADO   PIC XX.

       LINKAGE SECTION.

       77  NOMBREFILE           PIC X(256).

       PROCEDURE DIVISION USING NOMBREFILE.

       PROGRAMA SECTION.
       INICIALES.
       PROCESOS.
           MOVE SPACES TO BUFER.

           MOVE SPACES TO VALORDIR.
           MOVE SPACES TO NOMBRDIR.
           STRING "dd_" NOMBREFILE(1:3)
                  DELIMITED BY " "
                  INTO NOMBRDIR
           END-STRING.
           DISPLAY NOMBRDIR UPON ENVIRONMENT-NAME.
           ACCEPT VALORDIR FROM ENVIRONMENT-VALUE.
           IF VALORDIR NOT = SPACES AND
              VALORDIR NOT = LOW-VALUES
              MOVE SPACES TO BUFER
              STRING VALORDIR NOMBREFILE(4:)
                     DELIMITED BY " "
                     INTO BUFER
              END-STRING
           ELSE
              MOVE NOMBREFILE TO BUFER
           END-IF.

           CALL "CBL_DELETE_FILE" USING BUFER.*> ESTADO.

           STRING BUFER ".idx"
                  DELIMITED BY " "
                  INTO BUFER
           END-STRING.

           CALL "CBL_DELETE_FILE" USING BUFER.*> ESTADO.
       FIN-PROCESOS.
           EXIT PROGRAM.
       FIN-PROGRAMA.
           STOP RUN.

