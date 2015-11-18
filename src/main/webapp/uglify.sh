#!/bin/bash

if ! which uglifyjs >/dev/null 2>&1
then
	echo "uglifyjs is needed."
	echo "sudo npm install uglify-js -g"
	exit 1
fi

if ! which uglifycss >/dev/null 2>&1
then
	echo "uglifycss is needed."
	echo "sudo npm install uglifycss -g"
	exit 1
fi

cd $(dirname $0)/static

for js in $(find formsrc -name "*.js")
do
	to=form/${js##formsrc/}
	printf "uglify ${js} to ${to} ..."
	if uglifyjs ${js} -o ${to} -m
	then
		printf "OK.\n"
	else
		printf "ERROR.\n"
	fi
done

for css in $(find formsrc -name "*.css")
do
	to=form/${css##formsrc/}
	printf "uglify ${css} to ${to} ..."
	if uglifycss ${css} > ${to}
	then
		printf "OK.\n"
	else
		printf "ERROR.\n"
	fi
done

