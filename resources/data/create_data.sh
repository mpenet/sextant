#!/bin/sh
wget "http://download.geonames.org/export/dump/cities15000.zip"
wget "http://download.geonames.org/export/dump/allCountries.zip"
unzip allCountries.zip
alias tawk='awk -F"\t"'
cat allCountries.txt | tawk '{if ($7 == "A") print $0}' | grep PCLI   | tawk '{print $2"\t"$4"\t"$9"\t"$11"\tCountry\t"$15"\t"$5"\t"$6}' > countries.tsv
cat allCountries.txt | tawk '{if ($7 == "P") print $0}' | grep PPL    | tawk '{if( $(NF-4) >= 10000) print $0}' | tawk '{print $2"\t"$4"\t"$9"\t"$11"\tCity\t"$15"\t"$5"\t"$6}' > cities.tsv
cat allCountries.txt | tawk '{if ($7 == "A") print $0}' | grep "ADM1" | tawk '{print $2"\t"$4"\t"$9"\t"$11"\tState\t"$15"\t"$5"\t"$6}' > states.tsv
