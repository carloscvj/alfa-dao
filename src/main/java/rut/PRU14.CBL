       IDENTIFICATION DIVISION.

       PROGRAM-ID.    PRU14.
       AUTHOR.        CVJ.
       SECURITY.      DEVUELVE EL NOMBRE DE UN FICHERO TEMPORAL.

       ENVIRONMENT DIVISION.

       INPUT-OUTPUT SECTION. 
       FILE-CONTROL.

           SELECT TTY
                  ASSIGN TO  NOMBRETTY
                  ORGANIZATION IS INDEXED
                  ACCESS MODE IS DYNAMIC
                  RECORD KEY IS RTTY-1
                  LOCK MODE AUTOMATIC WITH LOCK ON RECORD
                  FILE STATUS IS ERRORES.

       DATA DIVISION.
       FILE SECTION.

       FD  TTY
           DATA RECORD IS RTTY.
       01  RTTY.
           03 RTTY-1 PIC X.
           03 RTTY-2 PIC 99999.

       WORKING-STORAGE SECTION.

             COPY "WO-ERRORES".

       77  DD-TMP PIC X(30).

       77  PROG  PIC X(10) VALUE "PRU14".

       77  ESTADO   PIC XX.
       77  ELNOMBRE PIC X(128).
       77  NAMF      PIC X(20).

       LINKAGE SECTION.

       77  NAMEFILE        PIC X(128).

       PROCEDURE DIVISION USING NAMEFILE.

       PROGRAMA SECTION.
       INICIALES.
           MOVE "tmp/N0" TO NOMBRETTY.
           CALL "PRUMKDIR" USING NOMBRETTY.
           OPEN I-O TTY.
           INITIALIZE RTTY.
       RELEE.
           READ TTY INVALID KEY
                INITIALIZE RTTY
                WRITE RTTY.
           IF ERRORES = "9D"
              GO TO RELEE.
           ADD 1 TO RTTY-2.
           REWRITE RTTY.
           CLOSE TTY.
       PROCESOS.
           MOVE "tmp/T" TO NAMF.
           STRING NAMF RTTY-2
                  DELIMITED BY " "
                  INTO NAMF
           END-STRING.
           MOVE NAMF TO NAMEFILE.

           MOVE NAMEFILE TO ELNOMBRE.
           CALL "CBL_DELETE_FILE" USING ELNOMBRE ESTADO.
           MOVE SPACES TO ELNOMBRE.
           STRING NAMEFILE ".idx"
                  DELIMITED BY " "
                  INTO ELNOMBRE
           END-STRING.
           CALL "CBL_DELETE_FILE" USING ELNOMBRE ESTADO.
       FIN-PROCESOS.
           EXIT PROGRAM.
       FIN-PROGRAMA.
           STOP RUN.

