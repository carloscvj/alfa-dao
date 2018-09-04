       IDENTIFICATION DIVISION.
       PROGRAM-ID. PRUIBANEUR.
       AUTHOR. IBH.
       DATE-WRITTEN. 29/11/2013.
       SECURITY. CALCULO DEL DC DEL IBAN DE CTAS EUROPEAS.
       SPECIAL-NAMES.   
               DECIMAL-POINT IS COMMA.
       DATA DIVISION.
       WORKING-STORAGE SECTION.
       77 PROG  PIC X(10) VALUE "PRUIBANEUR".
       01 LETRA PIC X.
       01 ASCII PIC 99 COMP-X REDEFINES LETRA.
       77 R1        PIC 999.
       01 DIVI.
          03 DIVI-I PIC 9(02).
          03 DIVI-N.
             05 DIVI-1 PIC 9(07).
             05 DIVI-2 PIC 9(02).
             05 DIVI-3 PIC 9(02).
             05 DIVI-4 PIC 9(02).
       77 DIVIDEND9 PIC 9(09).
       77 DIVIDENDO PIC 9(11).
       77 COCIENTE  PIC 9(11).
       77 RESTO     PIC 9(11).
       77 RSTDO     PIC 9(15).
       01 CT.
          03 CT-T   OCCURS 40.
             05 CT-C   PIC X.
       01 TB.
          03 TB-T   OCCURS 40.
             05  TB-D   PIC 9.
       01 CC.
          03 CC-N  PIC 99.
          03 CC-D REDEFINES CC-N.
             05 CC-D1 PIC 9.
             05 CC-D2 PIC 9.
       01 REFOR.
          03 REFOR1 OCCURS 28.
             05 REFOR1-C PIC X.
          03 REFOR2 PIC 99 VALUE 00.
          03 REFOR3 PIC 99 VALUE 00.
          03 REFOR4 PIC 99 VALUE 00.
       01 CALC.
          03 CALC-1 PIC 9(9).
          03 CALC-2 PIC 9(9).
          03 CALC-3 PIC 9(9).
          03 CALC-4 PIC 9(7).
       01 DIVI9.
          03 DIVI9-I PIC 9(02).
          03 DIVI9-N PIC 9(09).
       01 DIVI7.
          03 DIVI7-I PIC 9(02).
          03 DIVI7-N PIC 9(07).
       77 TT  PIC 99.
       77 NUM PIC 9.
       77 JJ PIC 99.
       77 KK PIC 99.
       LINKAGE SECTION.
       01 CUENTA.
          05 CTA1  PIC X(30).
       01 IBAN.
          03 TEXT-P PIC X(2).
          03 TEXT-D PIC 99.
          03 TEXT-C PIC X(30).
       01 PAIS.
          03 PAIS-1 PIC X.
          03 PAIS-2 PIC X.
       PROCEDURE DIVISION USING CUENTA IBAN PAIS.
       INICIO.

      * **** COMO SE CALCULA ? **********
      * SE CONVIERTE LAS LETRAS EN NUMERO TANTO LAS DE 
      * LA CUENTA COMO LAS DEL PAIS
      * A CADA LETRA SE LE HA ASIGNADO UN NUMERO OFICIAL
      * PARA NO TENER QUE PONER UNA TABLA HE UTILIZADO
      * EL CODIGO ASCII DE CADA LETRA 
      * Y RESTANDOLE 9 NOS DA EL NUMERO DE ESA TABLA
      * LA FORMA DE CALCULARLO ES EN MODULO 97
      * Y AL NUMERO A DIVIDIR HAY QUE ESTAR FORMATEADO DE 
      * LA SIGUIENTE MANERA 
      * CODIGO DE LA CUANTA EN NUMEROS 
      * CODIGO DEL PAIS EN NUMEROS
      * Y 00
      * EJEMPLO :
      *      CUENTA FRANCESA  20041010050500013 M 02606
      *      PAIS FR
      *      CONVIRTIENDO LAS LETRAS NOS DARIA:
      ******* EL 22 = M
      ******* EL 15 = F
      ******* EL 27 = R
      * RESULTADO FINAL 200410100505000132202606152700
      * COMO NO PODEMOS MANEJAR NUMEROS TAN GRANDES 
      * LO DIVIDO EN 4 PARTICIONES
      * CADA PARTICION SE DIVIDE ENTRE 97 
      * Y EL RESTO SE COLOCA AL INICIO DE LA SIGUIENTE PARTICION
      * Y EN LA CUARTA PARTICION SE LE RESTA A 98 EL RESULTADO 
      * ULTIMO Y ESTE SE CORRESPONDERA CON LOS DOS DIGITOS DE CONTROL
      * LA LONGITUD TOTAL DEL IBAN ES DE 34 CARACTERES
      * SI NO OCUAPA LOS 34 SE QUEDA EN BLANCO LOS 
      * CARACTERES FINALES DE LA CADENA

          MOVE 00 TO TT
          MOVE 00 TO JJ
          MOVE CUENTA TO CT
          PERFORM UNTIL JJ = 35
                  ADD 1 TO JJ
                  IF CT-C(JJ) NOT = " "
                     MOVE CT-C(JJ) TO LETRA
                     IF ASCII > 47 AND
                        ASCII < 58
                        COMPUTE NUM = ASCII - 48
                        PERFORM COLOCA-NUM
                      ELSE
                        COMPUTE CC-N =  ASCII - 64 + 9
                        MOVE CC-D1 TO NUM
                        PERFORM COLOCA-NUM
                        MOVE CC-D2 TO NUM
                        PERFORM COLOCA-NUM
                     END-IF
                  END-IF
          END-PERFORM.

          MOVE TT TO JJ
          MOVE 28 TO KK
          PERFORM UNTIL JJ = 00
             MOVE TB-D(JJ) TO REFOR1-C(KK)
             SUBTRACT 1 FROM KK
             SUBTRACT 1 FROM JJ
          END-PERFORM
          MOVE ZEROES TO KK
          COMPUTE JJ = 28 - TT
          PERFORM UNTIL KK = JJ
                  ADD 1 TO KK
                  MOVE "0" TO REFOR1-C(KK)
          END-PERFORM 

          MOVE PAIS-1 TO LETRA
          COMPUTE REFOR2 =  ASCII - 64 + 9

          MOVE PAIS-2 TO LETRA
          COMPUTE REFOR3 =  ASCII - 64 + 9

          MOVE ZEROES TO RSTDO.
          MOVE ZEROES TO RESTO
          MOVE REFOR TO CALC.
          MOVE 00    TO DIVI9-I.
          MOVE CALC-1 TO DIVI9-N.
          MOVE DIVI9 TO DIVIDENDO
          IF DIVIDENDO NOT = ZEROES
             DIVIDE DIVIDENDO 
                 BY 97 
                 GIVING COCIENTE
                 REMAINDER RESTO.

          MOVE RESTO TO DIVI9-I.
          MOVE ZEROES TO RESTO
          MOVE CALC-2 TO DIVI9-N.
          MOVE DIVI9 TO DIVIDENDO
          IF DIVIDENDO NOT = ZEROES
             DIVIDE DIVIDENDO 
                 BY 97 
                 GIVING COCIENTE
                 REMAINDER RESTO.
          
          MOVE RESTO TO DIVI9-I.
          MOVE ZEROES TO RESTO   
          MOVE CALC-3 TO DIVI9-N.
          MOVE DIVI9 TO DIVIDENDO
          IF DIVIDENDO NOT = ZEROES
             DIVIDE DIVIDENDO 
                 BY 97 
                 GIVING COCIENTE
                 REMAINDER RESTO.


          MOVE RESTO TO DIVI7-I.
          MOVE ZEROES TO RESTO
          MOVE CALC-4 TO DIVI7-N.
          MOVE DIVI7 TO DIVIDEND9
          IF DIVIDEND9 NOT = ZEROES
             DIVIDE DIVIDEND9 
                 BY 97 
                 GIVING COCIENTE
                 REMAINDER RESTO.
          COMPUTE RSTDO = 98 - RESTO.
 
          MOVE PAIS  TO TEXT-P
          MOVE RSTDO TO TEXT-D
          MOVE CTA1  TO TEXT-C
          GO TO FIN.
       COLOCA-NUM.
          ADD 1 TO TT
          MOVE NUM TO TB-D(TT).
       FIN.
          EXIT PROGRAM.
