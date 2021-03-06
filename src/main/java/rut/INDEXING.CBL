GCobol >>SOURCE FORMAT IS FIXED
      *> ***************************************************************
      *><* ================
      *><* indexing example
      *><* ================
      *><* :Author:    Brian Tiffin
      *><* :Date:      17-Feb-2009, 28-Jan-2014
      *><* :Purpose:   Demonstrate Indexed IO routines and START
      *><* :Tectonics: cobc -x indexing.cob
      *> ***************************************************************
       identification division.
       program-id. INDEXING.

       environment division.
       configuration section.

       input-output section.
       file-control.
          select optional indexed-file
          assign to "indexed-file"
          status is indexing-status
          organization is indexed
          access mode is dynamic
          record key is keyfield of indexing-record
          alternate record key is altkey of indexing-record
              with duplicates
          .

      *> ** GnuCOBOL only supports split keys **
      *> **    in the reportwriter branch     **
      *> **        see second listing         **

      *>
      *>  alternate record key is splitkey
      *>      source is first-part of indexing-record
      *>                last-part of indexing-record
      *>      with duplicates

       data division.
       file section.
       fd indexed-file.
       01 indexing-record.
          03 keyfield          pic x(8).
          03 filler            pic x.
          03 altkey.
             05 first-part     pic 99.
             05 middle-part    pic x.
             05 last-part      pic 99.
          03 filler            pic x.
          03 data-part         pic x(52).

       working-storage section.
       01 indexing-status.
          03 high-status-code  pic xx.
          03 high-status redefines high-status-code pic 99.
             88 indexing-ok              values 0 thru 10.
          03 low-status-code   pic xx.
          03 low-status redefines low-status-code pic 99.


       01 display-record.
          03 filler            pic x(4)  value spaces.
          03 keyfield          pic x(8).
          03 filler            pic xx    value spaces.
          03 altkey.
             05 first-part     pic 99.
             05 filler         pic x     value space.
             05 middle-part    pic x.
             05 filler         pic x     value space.
             05 last-part      pic 99.
          03 filler            pic xx    value ", ".
          03 data-part         pic x(52).

      *> control break
       01 oldkey               pic 99x99.

      *> read control fields
       01 duplicate-flag       pic x.
          88 no-more-duplicates          value high-value
             when set to false is              low-value.
       01 record-flag          pic x.
          88 no-more-records             value high-value
             when set to false is              low-value.

      *> ***************************************************************
       procedure division.

      *> Populate a sample database, create or overwrite keys
       perform populate-sample

      *> clear the record space for this example
       move spaces to indexing-record

      *> open the data file again
       open i-o indexed-file
       perform indexing-check

      *> bail if things are going wrong
       if not indexing-ok then
           display "error opening for read pass, stopping" upon syserr
           stop run returning 1
       end-if

      *> read all the duplicate 00b02 keys
       move 00 to first-part of indexing-record
       move "b" to middle-part of indexing-record
       move 02 to last-part of indexing-record

      *> using read key and then next key / last key compare
       set no-more-duplicates to false

       display "Read all 00b02 keys sequentially" end-display
       perform read-indexing-record
       perform read-next-record
           until no-more-duplicates
       display space end-display

      *> read by key of reference ... the cool stuff
       move 00 to first-part of indexing-record
       move "a" to middle-part of indexing-record
       move 02 to last-part of indexing-record
       set no-more-records to false

      *> using start and read next
       display "Read all alternate keys greater than 00a02" end-display
       perform start-at-key
       perform read-next-by-key
           until no-more-records
       display space end-display

      *> read by primary key of reference
       move "87654321" to keyfield of indexing-record
       set no-more-records to false

      *> using start and previous by key
       display
           "Read all primary keys less than "
           keyfield of indexing-record
       end-display
       perform start-prime-key
       perform read-previous-by-key
           until no-more-records
       display space end-display

      *> and with that we are done with indexing sample
       close indexed-file

       goback.
      *> ***************************************************************

      *> ***************************************************************
      *><* read by alternate key paragraph
       read-indexing-record.
           display "Reading: " altkey of indexing-record end-display
           read indexed-file key is altkey of indexing-record
               invalid key
                   display
                       "bad read key: " altkey of indexing-record
                       upon syserr
                   end-display
               set no-more-duplicates to true
           end-read
           perform indexing-check
       .

      *><* read next sequential paragraph
       read-next-record.
           move corresponding indexing-record to display-record
           display display-record end-display
           move altkey of indexing-record to oldkey

           read indexed-file next record
               at end set no-more-duplicates to true
               not at end
                   if oldkey not equal altkey of indexing-record
                       set no-more-duplicates to true
                   end-if
           end-read
           perform indexing-check
       .

      *><* start primary key of reference paragraph
       start-prime-key.
           display "Prime < " keyfield of indexing-record end-display
           start indexed-file
              key is less than
                  keyfield of indexing-record
              invalid key
                  display
                      "bad start: " keyfield of indexing-record
                      upon syserr
                  end-display
                  set no-more-records to true
              not invalid key
                  read indexed-file previous record
                      at end set no-more-records to true
                  end-read
           end-start
           perform indexing-check
       .

      *><* read previous by key of reference paragraph
       read-previous-by-key.
           move corresponding indexing-record to display-record
           display display-record end-display

           read indexed-file previous record
               at end set no-more-records to true
           end-read
           perform indexing-check
       .
      *><* start alternate key of reference paragraph
       start-at-key.
           display "Seeking >= " altkey of indexing-record end-display
           start indexed-file
              key is greater than or equal to
                  altkey of indexing-record
              invalid key
                  display
                      "bad start: " altkey of indexing-record
                      upon syserr
                  end-display
                  set no-more-records to true
              not invalid key
                  read indexed-file next record
                      at end set no-more-records to true
                  end-read
           end-start
           perform indexing-check
       .

      *><* read next by key of reference paragraph
       read-next-by-key.
           move corresponding indexing-record to display-record
           display display-record end-display

           read indexed-file next record
               at end set no-more-records to true
           end-read
           perform indexing-check
       .

      *><* populate a sample database
       populate-sample.

      *> Open optional index file for read write
           open i-o indexed-file
           perform indexing-check

           move "12345678 00a01 some 12345678 data" to indexing-record
           perform write-indexing-record
           move "87654321 00a01 some 87654321 data" to indexing-record
           perform write-indexing-record
           move "12348765 00a01 some 12348765 data" to indexing-record
           perform write-indexing-record
           move "87651234 00a01 some 87651234 data" to indexing-record
           perform write-indexing-record

           move "12345679 00b02 some 12345679 data" to indexing-record
           perform write-indexing-record
           move "97654321 00b02 some 97654321 data" to indexing-record
           perform write-indexing-record
           move "12349765 00b02 some 12349765 data" to indexing-record
           perform write-indexing-record
           move "97651234 00b02 some 97651234 data" to indexing-record
           perform write-indexing-record

           move "12345689 00c13 some 12345689 data" to indexing-record
           perform write-indexing-record
           move "98654321 00c13 some 98654321 data" to indexing-record
           perform write-indexing-record
           move "12349865 00c13 some 12349865 data" to indexing-record
           perform write-indexing-record
           move "98651234 00c13 some 98651234 data" to indexing-record
           perform write-indexing-record

      *> close it ... not necessary, but for the example we will
           close indexed-file
           perform indexing-check
       .

      *><* Write paragraph
       write-indexing-record.
           write indexing-record
               invalid key
                   display
                       "rewriting key: " keyfield of indexing-record
                       upon syserr
                   end-display
                   rewrite indexing-record
                       invalid key
                           display
                               "really bad key: "
                               keyfield of indexing-record
                               upon syserr
                           end-display
                   end-rewrite
           end-write
       .

      *><* file status quick check.  For this sample, keep running
       indexing-check.
           if not indexing-ok then
               display
                   "isam file io problem: " indexing-status
                   upon syserr
               end-display
           end-if
       .

       end program INDEXING.
      *><*
      *><* Last Update: 20140128
