       IDENTIFICATION DIVISION.

       PROGRAM-ID.    PRUMKDIR.
       AUTHOR.        carlos.
       SECURITY.      HACE DIRECTORIO PARA NOMBREFICHERO CON "dir" O "tmp" O "obj" POR DELANTE.

       ENVIRONMENT DIVISION.
       DATA DIVISION.
       WORKING-STORAGE SECTION.

           COPY "WO-ERRORES".

       77  II                PIC 999.
       77  ESTADO            PIC XX.

       01  VALORDIR          PIC X(128).
       01  NOMBRDIR          PIC X(128).

       01 BUFER-DIR.
          03 BUF-1 PIC X OCCURS 256.
       01 DIRECTORIO.
          03 DIR-1 PIC X OCCURS 256.

       LINKAGE SECTION.
       01 NOMBREFICHERO PIC X(256).
       PROCEDURE DIVISION USING NOMBREFICHERO.
       PROGRAMA SECTION.
       INICIALES.
       PROCESOS.
           PERFORM HACDIR.
       FIN-PROCESOS.
           EXIT PROGRAM.
       FIN-PROGRAMA.
           STOP RUN.

       HACDIR SECTION.
       HACIENDODIR.
           MOVE SPACES TO VALORDIR.
           MOVE SPACES TO NOMBRDIR.
           STRING "dd_" NOMBREFICHERO(1:3)
                  DELIMITED BY " "
                  INTO NOMBRDIR
           END-STRING.
           DISPLAY NOMBRDIR UPON ENVIRONMENT-NAME.
           ACCEPT VALORDIR FROM ENVIRONMENT-VALUE.
           IF VALORDIR NOT = SPACES AND
              VALORDIR NOT = LOW-VALUES
              MOVE SPACES TO BUFER-DIR
              STRING VALORDIR NOMBREFICHERO(4:)
                     DELIMITED BY " "
                     INTO BUFER-DIR
              END-STRING
           ELSE
              MOVE NOMBREFICHERO TO BUFER-DIR
           END-IF.
           DISPLAY "CREANDO DIR:" BUFER-DIR UPON SYSOUT.
           MOVE SPACES TO DIRECTORIO.
           PERFORM VARYING II FROM 1 BY 1 UNTIL II > 256
                   IF BUF-1(II) = "/" OR "\"
                      CALL "CBL_CREATE_DIR" USING DIRECTORIO *> ESTADO
                   END-IF
                   MOVE BUF-1(II) TO DIR-1(II)
                   IF DIR-1(II) = " "
                      MOVE 256 TO II
                   END-IF
           END-PERFORM.
       FIN-HACDIR.
