       IDENTIFICATION DIVISION.

       PROGRAM-ID.    PRUPARJ.  
       AUTHOR.        JAC.
       SECURITY.      LLAMA PROGRAMA DESDE LINEA COMANDO.

       ENVIRONMENT DIVISION.

       INPUT-OUTPUT SECTION. 
       FILE-CONTROL.

       DATA DIVISION.
       FILE SECTION.

       WORKING-STORAGE SECTION.

             COPY "WO-ERRORES".

       77  ESCRIBIBLE      PIC X     IS EXTERNAL.
       77  USUARIO         PIC X(15) IS EXTERNAL.
       77  LONGI           PIC 99999.
       77  TECLA           PIC X.
       77  ESTADO          PIC XX.
       77  II              PIC 99.
       77  NPAR            PIC 99.
       77  NREAL-PAR       PIC 999.
       77  NOMBRE          PIC X(50).
       77  CUAL            PIC X(50).
       77  PARAMETRO-PROGRAMA PIC X(1024).
       77  X-ERROR            PIC X(50).
       77  CUANTOS            PIC 99999.
       77  BUFERENT           PIC X(1024).
       01  LINEA-PAR.
           03 LI-PA-O         OCCURS 15.
              05 PAR          PIC X(1024).

       01  REALES-PAR.
           03 RE-PA-O         OCCURS 128.
              05 REAL-PAR     PIC X(1024).

       LINKAGE SECTION.
       77  ALLAMAR  PIC X(128).
       PROCEDURE DIVISION USING ALLAMAR.

       PROGRAMA SECTION.
       INICIALES.
           MOVE ZEROES TO NREAL-PAR.
           INITIALIZE REALES-PAR.
       ACCEPTANDO.
           IF ESCRIBIBLE = "P"
              CALL "Leer" USING BUFERENT
           ELSE
              ACCEPT BUFERENT FROM SYSIN
           END-IF.

           INSPECT BUFERENT REPLACING ALL X"00" BY " ".
           EVALUATE BUFERENT
              WHEN "LLAMA@" GO TO PROCESOS
           END-EVALUATE.

           MOVE BUFERENT TO PARAMETRO-PROGRAMA.

           UNSTRING PARAMETRO-PROGRAMA DELIMITED BY " " INTO
                    PAR(01)
                    PAR(02)
                    PAR(03)
                    PAR(04)
                    PAR(05)
                    PAR(06)
                    PAR(07)
                    PAR(08)
                    PAR(09)
                    PAR(10)
                    PAR(11)
                    PAR(12)
                    PAR(13)
                    PAR(14)
                    PAR(15)
           END-UNSTRING.
           MOVE ZEROES TO NPAR.
           PERFORM VARYING II FROM 1 BY 1 UNTIL II > 15
              IF PAR(II) NOT = SPACES
                 ADD 1 TO NPAR
              END-IF
              INSPECT PAR(II) REPLACING ALL "#" BY " "
              IF PAR(II) NOT = SPACES
                 ADD 1 TO NREAL-PAR
                 MOVE PAR(II) TO REAL-PAR(NREAL-PAR)
              END-IF
           END-PERFORM.
           IF PARAMETRO-PROGRAMA = SPACES
              ADD 1 TO NREAL-PAR
           END-IF.

           GO TO ACCEPTANDO.
       PROCESOS.
 

           CALL ALLAMAR USING
                REAL-PAR(01)
                REAL-PAR(02)
                REAL-PAR(03)
                REAL-PAR(04)
                REAL-PAR(05)
                REAL-PAR(06)
                REAL-PAR(07)
                REAL-PAR(08)
                REAL-PAR(09)
                REAL-PAR(10)
                REAL-PAR(11)
                REAL-PAR(12)
                REAL-PAR(13)
                REAL-PAR(14)
                REAL-PAR(15)
                REAL-PAR(16)
                REAL-PAR(17)
                REAL-PAR(18)
                REAL-PAR(19)
                REAL-PAR(20)
                REAL-PAR(21)
                REAL-PAR(22)
                REAL-PAR(23)
                REAL-PAR(24)
                REAL-PAR(25)
                REAL-PAR(26)
                REAL-PAR(27)
                REAL-PAR(28)
                REAL-PAR(29)
                REAL-PAR(30)
                REAL-PAR(31)
                REAL-PAR(32)
                REAL-PAR(33)
                REAL-PAR(34)
                REAL-PAR(35)
                REAL-PAR(36)
                REAL-PAR(37)
                REAL-PAR(38)
                REAL-PAR(39)
                REAL-PAR(40)
                REAL-PAR(41)
                REAL-PAR(42)
                REAL-PAR(43)
                REAL-PAR(44)
                REAL-PAR(45)
                REAL-PAR(46)
                REAL-PAR(47)
                REAL-PAR(48)
                REAL-PAR(49)
                REAL-PAR(50)
                REAL-PAR(51)
                REAL-PAR(52)
                REAL-PAR(53)
                REAL-PAR(54)
                REAL-PAR(55)
                REAL-PAR(56)
                REAL-PAR(57)
                REAL-PAR(58)
                REAL-PAR(59)
                REAL-PAR(60)
                REAL-PAR(61)
                REAL-PAR(62)
                REAL-PAR(63)
                REAL-PAR(64)
                REAL-PAR(65)
                REAL-PAR(66)
                REAL-PAR(67)
                REAL-PAR(68)
                REAL-PAR(69)
                REAL-PAR(70)
                REAL-PAR(71)
                REAL-PAR(72)
                REAL-PAR(73)
                REAL-PAR(74)
                REAL-PAR(75)
                REAL-PAR(76)
                REAL-PAR(77)
                REAL-PAR(78)
                REAL-PAR(79)
                REAL-PAR(80)
                REAL-PAR(81)
                REAL-PAR(82)
                REAL-PAR(83)
                REAL-PAR(84)
                REAL-PAR(85)
                REAL-PAR(86)
                REAL-PAR(87)
                REAL-PAR(88)
                REAL-PAR(89)
                REAL-PAR(90)
                REAL-PAR(91)
                REAL-PAR(92)
                REAL-PAR(93)
                REAL-PAR(94)
                REAL-PAR(95)
                REAL-PAR(96)
                REAL-PAR(97)
                REAL-PAR(98)
                REAL-PAR(99)
                REAL-PAR(100)
                REAL-PAR(101)
                REAL-PAR(102)
                REAL-PAR(103)
                REAL-PAR(104)
                REAL-PAR(105)
                REAL-PAR(106)
                REAL-PAR(107)
                REAL-PAR(108)
                REAL-PAR(109)
                REAL-PAR(110)
                REAL-PAR(111)
                REAL-PAR(112)
                REAL-PAR(113)
                REAL-PAR(114)
                REAL-PAR(115)
                REAL-PAR(116)
                REAL-PAR(117)
                REAL-PAR(118)
                REAL-PAR(119)
                REAL-PAR(120)
                REAL-PAR(121)
                REAL-PAR(122)
                REAL-PAR(123)
                REAL-PAR(124)
                REAL-PAR(125)
                REAL-PAR(126)
                REAL-PAR(127)
                REAL-PAR(128).

       FIN-PROCESOS.
           MOVE 5 TO LONGI.
           IF ESCRIBIBLE = "P"
              CALL "Escribir" USING "|FIN|" LONGI
           ELSE
              DISPLAY "|FIN|" UPON SYSOUT
           END-IF.


           EXIT PROGRAM.
       FIN-PROGRAMA.
           STOP RUN.

