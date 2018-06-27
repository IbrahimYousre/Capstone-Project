const functions = require('firebase-functions');
const admin = require('firebase-admin');

admin.initializeApp();

exports.addUser = functions.auth.user().onCreate(user => {
  console.log('A new user signed in for the first time.');
  return admin.database().ref('users').push({
    name: user.displayName || 'Anonymous',
    photoUrl: user.photoURL || 'https://ca.slack-edge.com/T95Q9N50F-U98GDP28G-g2e374b00968-48',
    text: `New User`,
  }).then(() => {
    console.log('User added successfully');
    return;
  });
});

exports.newAnswer = functions.database.ref('answers/{answerId}/userId').onCreate((snapshot, context)=>{
  console.log('new answer by user '+snapshot.val());
  return admin.database().ref('user_answers').child(snapshot.val()).child(context.params.answerId).set(true);
});

exports.updateUserInfo = functions.database.ref('users/{userId}').onUpdate((change, context)=>{
  let userId = context.params.userId;
  let user = change.after.val();
  let name = user.name;
  let photoUrl = user.photoUrl;
  let title = user.title;

  console.log(`updating user info ${userId}`);

  return admin.database().ref(`user_answers/${userId}`).once('value').then( snap => {
    let answerKeys = Object.keys(snap.val());
    let updateObj = {};
    
    let n = answerKeys.length;
    console.log(`updating ${n} answers`);

    answerKeys.forEach(key => {
      updateObj[`answers/${key}/userId`] = userId;
      updateObj[`answers/${key}/userName`] = name;
      updateObj[`answers/${key}/userPhotoUrl`] = photoUrl;
      updateObj[`answers/${key}/userTitle`] = title;
    });

    return admin.database().ref().update(updateObj);
  });
});