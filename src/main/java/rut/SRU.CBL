       IDENTIFICATION DIVISION.

       PROGRAM-ID.    SRU.
       AUTHOR.        CVJ.
       SECURITY.      El que llama a los servidores de cobol.

       DATA DIVISION.

       WORKING-STORAGE SECTION.

       01 USUARIO       PIC X(15) IS EXTERNAL.
       01 PALPRUPARJ    PIC X(128).
       01 OPERACION                   PIC X(32).
       01 VALORES                     PIC X(32).
       01 BUFERENT                    PIC X(1024).
       01 SEPARADOR PIC XXX VALUE "|@|".
       77  II PIC 99.

       PROCEDURE DIVISION.

       PROGRAMA SECTION.
       LIMPIEZA.

           INITIALIZE USUARIO
                      PALPRUPARJ
                      OPERACION
                      VALORES
                      BUFERENT
                      II.

       PROCESOS.
           ACCEPT BUFERENT FROM SYSIN
           INSPECT BUFERENT REPLACING ALL X"00" BY " ".
           INITIALIZE OPERACION VALORES PALPRUPARJ.
           PERFORM COGE-TRABAJO.
           EVALUATE OPERACION
                    WHEN "STP"  GO TO FIN-PROCESOS
                    WHEN "PAR"  GO TO FIN-PROCESOS
                    WHEN "LLA"  PERFORM LLAMAR
                    WHEN "CAN"  PERFORM CANCELAR
                    WHEN "login" 
                         MOVE VALORES TO USUARIO
                    WHEN "dd_dir" 
                         DISPLAY "dd_dir" UPON ENVIRONMENT-NAME
                         DISPLAY VALORES  UPON ENVIRONMENT-VALUE
                    WHEN "dd_mnu"
                         DISPLAY "dd_mnu" UPON ENVIRONMENT-NAME
                         DISPLAY VALORES  UPON ENVIRONMENT-VALUE
                    WHEN "dd_trz"
                         DISPLAY "dd_trz" UPON ENVIRONMENT-NAME
                         DISPLAY VALORES  UPON ENVIRONMENT-VALUE
                    WHEN "dd_tmp"
                         DISPLAY "dd_tmp" UPON ENVIRONMENT-NAME
                         DISPLAY VALORES  UPON ENVIRONMENT-VALUE
                    WHEN "dd_rom"
                         DISPLAY "dd_rom" UPON ENVIRONMENT-NAME
                         DISPLAY VALORES  UPON ENVIRONMENT-VALUE
                    WHEN "dd_obj"
                         DISPLAY "dd_obj" UPON ENVIRONMENT-NAME
                         DISPLAY VALORES  UPON ENVIRONMENT-VALUE

           END-EVALUATE.
           GO TO PROCESOS.
       FIN-PROCESOS.
           DISPLAY "|FIN|" UPON SYSOUT.
           EXIT PROGRAM.
       FIN-PROGRAMA.
           STOP RUN.

       COGE-TRABAJO SECTION.
       COG-TRA.
           UNSTRING BUFERENT DELIMITED BY SEPARADOR INTO
                 OPERACION
                 VALORES
                 PALPRUPARJ
           END-UNSTRING.
           
       FIN-COGE-TRABAJO.

       LLAMAR SECTION.
       LLAMANDO.
           CALL VALORES USING PALPRUPARJ.
       FIN-LLAMAR.

       CANCELAR SECTION.
       CANCELANDO.
           CANCEL VALORES.
       FIN-CANCELAR.

