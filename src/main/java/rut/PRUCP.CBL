       IDENTIFICATION DIVISION.

       PROGRAM-ID.    PRUCP.
       AUTHOR.        carlos.
       SECURITY.      COPIAR UN FICHERO A OTRO(incluido.idx y hacer directorio)

       ENVIRONMENT DIVISION.
       DATA DIVISION.
       WORKING-STORAGE SECTION.

           COPY "WO-ERRORES".

       77  PROG     PIC X(10) VALUE "PRUCP".
       77  II       PIC 99.
       77  JJ       PIC 99.
       01  VALORDIR          PIC X(128).
       01  NOMBRDIR          PIC X(128).

       01  BUFER1.
           03 BUF-1        PIC X      OCCURS 256.

       01  BUFER2.
           03 BUF-2        PIC X      OCCURS 256.

       77  ESTADO   PIC XX.

       LINKAGE SECTION.

       77  NOMBREFILE1           PIC X(256).
       77  NOMBREFILE2           PIC X(256).

       PROCEDURE DIVISION USING NOMBREFILE1 NOMBREFILE2.

       PROGRAMA SECTION.
       INICIALES.
           CALL "PRUMKDIR" USING NOMBREFILE2.
       PROCESOS.
           MOVE SPACES TO BUFER1 BUFER2.

           MOVE SPACES TO VALORDIR.
           MOVE SPACES TO NOMBRDIR.
           STRING "dd_" NOMBREFILE1(1:3)
                  DELIMITED BY " "
                  INTO NOMBRDIR
           END-STRING.
           DISPLAY NOMBRDIR UPON ENVIRONMENT-NAME.
           ACCEPT VALORDIR FROM ENVIRONMENT-VALUE.
           IF VALORDIR NOT = SPACES AND
              VALORDIR NOT = LOW-VALUES
              MOVE SPACES TO BUFER1
              STRING VALORDIR NOMBREFILE1(4:)
                     DELIMITED BY " "
                     INTO BUFER1
              END-STRING
           ELSE
              MOVE NOMBREFILE1 TO BUFER1
           END-IF.

           MOVE SPACES TO VALORDIR.
           MOVE SPACES TO NOMBRDIR.
           STRING "dd_" NOMBREFILE2(1:3)
                  DELIMITED BY " "
                  INTO NOMBRDIR
           END-STRING.
           DISPLAY NOMBRDIR UPON ENVIRONMENT-NAME.
           ACCEPT VALORDIR FROM ENVIRONMENT-VALUE.
           IF VALORDIR NOT = SPACES AND
              VALORDIR NOT = LOW-VALUES
              MOVE SPACES TO BUFER2
              STRING VALORDIR NOMBREFILE2(4:)
                     DELIMITED BY " "
                     INTO BUFER2
              END-STRING
           ELSE
              MOVE NOMBREFILE2 TO BUFER2
           END-IF.

           CALL "CBL_COPY_FILE" USING BUFER1 BUFER2.

           STRING BUFER1 ".idx"
                  DELIMITED BY " "
                  INTO BUFER1
           END-STRING.

           STRING BUFER2 ".idx"
                  DELIMITED BY " "
                  INTO BUFER2
           END-STRING.

           CALL "CBL_COPY_FILE" USING BUFER1 BUFER2.
       FIN-PROCESOS.
           EXIT PROGRAM.
       FIN-PROGRAMA.
           STOP RUN.

