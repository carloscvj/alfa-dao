       IDENTIFICATION DIVISION.

       PROGRAM-ID.    PRUSERL.
       AUTHOR.        CVJ.
       SECURITY.      DEVUELVE EL USER QUE HA ENTRADO EN EL LOGIN X(20).

       ENVIRONMENT DIVISION.

       DATA DIVISION.

       WORKING-STORAGE SECTION.
       77  PROG  PIC X(10) VALUE "PRUSERL".
       LINKAGE SECTION.

       77  NAMEUSER        PIC X(20).

       PROCEDURE DIVISION USING NAMEUSER.

       PROGRAMA SECTION.
       INICIALES.
       PROCESOS.
           DISPLAY "LOGNAME" UPON ENVIRONMENT-NAME.
           ACCEPT NAMEUSER FROM ENVIRONMENT-VALUE.
       FIN-PROCESOS.
           EXIT PROGRAM.
       FIN-PROGRAMA.
           STOP RUN.

