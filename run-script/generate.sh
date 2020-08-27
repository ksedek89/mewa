#!/bin/bash

CURRENT_DIR=$(pwd)
RELATIVE_DIR=$(dirname "$0")
CONFIG_FILE="$CURRENT_DIR/$RELATIVE_DIR/config.properties"

function getProperty {
   PROP_KEY=$1
   PROP_VALUE=`cat $CONFIG_FILE | grep "$PROP_KEY" | cut -d'=' -f2`
   echo $PROP_VALUE
}

DOCKER_LOCAL_IMAGE_PREFIX=$(getProperty "DOCKER_LOCAL_IMAGE_PREFIX")
echo "DOCKER_LOCAL_IMAGE_PREFIX=$DOCKER_LOCAL_IMAGE_PREFIX"
sed -i "s/localImagePrefix = 'starter'/localImagePrefix = '$DOCKER_LOCAL_IMAGE_PREFIX'/g" "$CURRENT_DIR/$RELATIVE_DIR/../build.gradle"

CONTEXT_PATH=$(getProperty "CONTEXT_PATH")
echo "CONTEXT_PATH=$CONTEXT_PATH"
sed -i "s|context-path: /starter|context-path: $CONTEXT_PATH|g" "$CURRENT_DIR/$RELATIVE_DIR/../portal-be-starter-main/application/src/main/resources/application.yml"

APPLICATION_DESC=$(getProperty "APPLICATION_DESC")
echo "APPLICATION_DESC=$APPLICATION_DESC"
sed -i "s/name: Portal Starter/name: $APPLICATION_DESC/g" "$CURRENT_DIR/$RELATIVE_DIR/../portal-be-starter-main/application/src/main/resources/application.yml"

APPLICATION_NAME=$(getProperty "APPLICATION_NAME")
echo "APPLICATION_NAME=$APPLICATION_NAME"
sed -i "s/name: STARTER/name: $APPLICATION_NAME/g" "$CURRENT_DIR/$RELATIVE_DIR/../portal-be-starter-main/application/src/main/resources/application.yml"

GIT_REPO_NAME=$(getProperty "GIT_REPO_NAME")
echo "GIT_REPO_NAME=$GIT_REPO_NAME"
sed -i "s/portal-be-starter/$GIT_REPO_NAME/g" "$CURRENT_DIR/$RELATIVE_DIR/../Jenkinsfile"
sed -i "s/portal-be-starter/$GIT_REPO_NAME/g" "$CURRENT_DIR/$RELATIVE_DIR/../settings.gradle"
find . -type f -name 'build.gradle' | xargs sed -i "s/portal-be-starter/$GIT_REPO_NAME/g"

PACKAGE_NAME=$(getProperty "PACKAGE_NAME")
echo "PACKAGE_NAME=$PACKAGE_NAME"
find . -type f -name '*.java' | xargs sed -i "s/com.pru.starter/com.pru.$PACKAGE_NAME/g"
sed -i "s/com.pru.starter/com.pru.$PACKAGE_NAME/g" "$CURRENT_DIR/$RELATIVE_DIR/../build.gradle"
sed -i "s/com.pru.starter/com.pru.$PACKAGE_NAME/g" "$CURRENT_DIR/$RELATIVE_DIR/../portal-be-starter-main/application/src/main/resources/ehcache.xml"

for f in `find portal-be-starter-main -type d -name starter`; do
    mv -- "$f" "${f%/starter}/$PACKAGE_NAME"
done
mv portal-be-starter-main "$GIT_REPO_NAME-main"

echo "TODO" > "$CURRENT_DIR/$RELATIVE_DIR/../README.md"

GIT_PROJECT_PREFIX_URL=$(getProperty "GIT_PROJECT_PREFIX_URL")
echo "Perform GIT changes"
git checkout -b "$GIT_REPO_NAME-starter"
git add . # stage all changed files
git reset -- "$CURRENT_DIR/$RELATIVE_DIR/../run-script" # exclude unwanted files
git commit -m "Initial $GIT_REPO_NAME commit" # initial commit for a new repo
git tag -a "initial" -m "initial" # tag initial commit
git remote set-url origin "$GIT_PROJECT_PREFIX_URL/$GIT_REPO_NAME.git" # change origin remote to a new one
git remote add starter "$GIT_PROJECT_PREFIX_URL/portal-be-starter.git" # add old origin as a second remote
git push --follow-tags "$GIT_PROJECT_PREFIX_URL/$GIT_REPO_NAME.git" # push to the new origin

echo "Self destroying..."
rm -rf "$CURRENT_DIR/$RELATIVE_DIR/../run-script"

echo "Project generated"
