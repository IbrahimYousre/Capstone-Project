const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();

// insert user entry into the database
exports.addUser = functions.auth.user().onCreate(user => {
  console.log(`New user is created (userId = ${user.uid})`);
  return admin.database().ref('users').child(user.uid).set({
    name: user.displayName || 'Anonymous',
    photoUrl: user.photoURL || 'https://ca.slack-edge.com/T95Q9N50F-U98GDP28G-g2e374b00968-48',
    text: 'New User',
  });
});

exports.addTimestampToAnswer = functions.database.ref('answers/{answerId}').onCreate((snapshot, context)=>{
  let answerId = context.params.answerId;
  return admin.database().ref(`answers/${answerId}/timestamp`).set(admin.database.ServerValue.TIMESTAMP);
});

// add answer to user answers for fast retreival when updating user information
exports.addAnswerToUser = functions.database.ref('answers/{answerId}/userId').onCreate((snapshot, context)=>{
  console.log(`New answer created by user (userId = ${snapshot.val()}`);
  return admin.database().ref('user_answers').child(snapshot.val()).child(context.params.answerId).set(true);
});

// when user update his profile we update all denormalized data in answers
exports.updateUserInfo = functions.database.ref('users/{userId}').onUpdate((change, context)=>{
  let userId = context.params.userId;
  let user = change.after.val();
  let old = change.before.val();
  let name = user.name;
  let photoUrl = user.photoUrl;
  let title = user.title;
  console.log(`Updating profile for user (userId = ${userId})`);
  return admin.database().ref(`user_answers/${userId}`).once('value').then( snap => {
    let answerKeys = Object.keys(snap.val());
    let updateObj = {};
    let n = answerKeys.length;
    console.log(`updating ${n} answers for user (userId = ${userId})`);
    answerKeys.forEach(key => {
      if(old.name !== name)
        updateObj[`answers/${key}/userName`] = name;
      if(old.photoUrl !== photoUrl)
        updateObj[`answers/${key}/userPhotoUrl`] = photoUrl;
      if(old.title !== title)
        updateObj[`answers/${key}/userTitle`] = title;
    });
    return admin.database().ref().update(updateObj);
  });
});