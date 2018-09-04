       IDENTIFICATION DIVISION.

       PROGRAM-ID.    QUITABLANCOS.
       AUTHOR.        carlos.
       SECURITY.      DEPURAR UN FICHERO EXCLUYENDO BLANCOS AL FINAL Y AL PRINCIPIO DE LOS CAMPOS.
                      LOS CAMPOS SEPARADOS POR: 77 SEPARADOR PIC X. DE LA LINKAGE.

       ENVIRONMENT DIVISION.

       INPUT-OUTPUT SECTION.
       FILE-CONTROL.

           SELECT FORIGEN
                  ASSIGN TO RANDOM NOMBREFILE1
                  ORGANIZATION IS LINE SEQUENTIAL
                  FILE STATUS IS ERRORES.
           SELECT FDESTINO
                  ASSIGN TO RANDOM NOMBREFILE2
                  ORGANIZATION IS LINE SEQUENTIAL
                  FILE STATUS IS ERRORES.

       DATA DIVISION.
       FILE SECTION.

       FD  FORIGEN
           DATA RECORD IS RORIGEN.
       01  RORIGEN.
           03 RORIGEN-1     PIC X(1024).
       FD  FDESTINO
           DATA RECORD IS RDESTINO.
       01  RDESTINO.
           03 RDESTINO-1    PIC X(1024).

       WORKING-STORAGE SECTION.

           COPY "WO-ERRORES".

       77  PROG     PIC X(20) VALUE "QUITABLANCOS".
       77  IORI     PIC 9999.
       77  IDES     PIC 9999.
       77  ENCAMPO  PIC 9.
       77  UNX      PIC X.
       77  NOMBREFILE1     PIC X(256).
       77  NOMBREFILE2     PIC X(256).
       77  NOMBRESWAP      PIC X(256).

       LINKAGE SECTION.

       77  SEPARADOR    PIC X.
       77  NOMBREVIENE  PIC X(256).

       PROCEDURE DIVISION USING SEPARADOR NOMBREVIENE.

       PROGRAMA SECTION.
       INICIALES.
           MOVE NOMBREVIENE TO NOMBREFILE1.
           CALL "PRU14" USING NOMBREFILE2.
           DISPLAY "DEBUG:DEPURANDO ESPACIOS EN BLANCO...".
       PROCESOS.
           OPEN INPUT FORIGEN.
           IF ERRORES NOT = "00"
              GO TO FIN-PROCESOS.
           OPEN OUTPUT FDESTINO.
           IF ERRORES NOT = ZEROES
              CLOSE FORIGEN
              GO TO FIN-PROCESOS.

       LEER.
           READ FORIGEN AT END
                GO TO CERRAR.

           INITIALIZE RDESTINO.
           MOVE 1025 TO IDES.
           MOVE 0 TO ENCAMPO.

           PERFORM VARYING IORI FROM 1024 BY -1 UNTIL IORI < 1
              MOVE RORIGEN(IORI:1) TO UNX
              IF UNX = " "
                 IF ENCAMPO = 1
                    SUBTRACT 1 FROM IDES
                    MOVE UNX TO RDESTINO(IDES:1)
                 END-IF
              END-IF
              IF UNX NOT = " "
                 SUBTRACT 1 FROM IDES
                 MOVE UNX TO RDESTINO(IDES:1)
                 MOVE 1 TO ENCAMPO
              END-IF
              IF UNX = SEPARADOR
                 MOVE 0 TO ENCAMPO
              END-IF                
           END-PERFORM.
           
           WRITE RDESTINO.

           GO TO LEER.
       CERRAR.
           CLOSE FORIGEN.
           CLOSE FDESTINO.
       CAMBIARLOSNOMBRES.
           MOVE NOMBREFILE1 TO NOMBRESWAP.
           MOVE NOMBREFILE2 TO NOMBREFILE1.
           MOVE NOMBRESWAP  TO NOMBREFILE2.
       VOLVER-A-EMPEZAR.
           OPEN INPUT FORIGEN.
           IF ERRORES NOT = "00"
              GO TO FIN-PROCESOS.
           OPEN OUTPUT FDESTINO.
           IF ERRORES NOT = ZEROES
              CLOSE FORIGEN
              GO TO FIN-PROCESOS.
       RE-LEER.
           READ FORIGEN AT END
                GO TO RE-CERRAR.

           INITIALIZE RDESTINO.
           MOVE ZEROES TO IDES.
           MOVE 0 TO ENCAMPO.

           PERFORM VARYING IORI FROM 1 BY 1 UNTIL IORI > 1024
              MOVE RORIGEN(IORI:1) TO UNX
              IF UNX = " "
                 IF ENCAMPO = 1
                    ADD 1 TO IDES
                    MOVE UNX TO RDESTINO(IDES:1)
                 END-IF
              END-IF
              IF UNX NOT = " "
                 ADD 1 TO IDES
                 MOVE UNX TO RDESTINO(IDES:1)
                 MOVE 1 TO ENCAMPO
              END-IF
              IF UNX = SEPARADOR
                 MOVE 0 TO ENCAMPO
              END-IF                
           END-PERFORM.

           WRITE RDESTINO.

           GO TO RE-LEER.
       RE-CERRAR.
           CLOSE FORIGEN.
           CLOSE FDESTINO.

       FIN-PROCESOS.
           EXIT PROGRAM.
       FIN-PROGRAMA.
           STOP RUN.

