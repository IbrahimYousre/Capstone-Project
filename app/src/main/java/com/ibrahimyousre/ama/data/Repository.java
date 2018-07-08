package com.ibrahimyousre.ama.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.ibrahimyousre.ama.data.model.Answer;
import com.ibrahimyousre.ama.data.model.Question;
import com.ibrahimyousre.ama.data.model.Topic;
import com.ibrahimyousre.ama.data.model.User;

import java.util.List;

import static com.ibrahimyousre.ama.data.DatabaseConstants.FIELD_QUESTION_ID;
import static com.ibrahimyousre.ama.data.DatabaseConstants.FIELD_TOPIC_ID;
import static com.ibrahimyousre.ama.data.DatabaseConstants.FIELD_USER_ID;
import static com.ibrahimyousre.ama.data.DatabaseConstants.PATH_ANSWERS;
import static com.ibrahimyousre.ama.data.DatabaseConstants.PATH_QUESTION_USERS;
import static com.ibrahimyousre.ama.data.DatabaseConstants.PATH_TOPICS;
import static com.ibrahimyousre.ama.data.DatabaseConstants.PATH_TOPIC_QUESTIONS;
import static com.ibrahimyousre.ama.data.DatabaseConstants.PATH_USERS;
import static com.ibrahimyousre.ama.data.DatabaseConstants.PATH_USER_NOTIFICATIONS;
import static com.ibrahimyousre.ama.data.DatabaseConstants.PATH_USER_TOPICS;

public class Repository {

    private final static Object LOCK = new Object();
    private static Repository sInstance;

    private final FirebaseDatabase firebaseDatabase;

    private Repository() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
    }

    public static Repository getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new Repository();
            }
        }
        return sInstance;
    }

    public LiveData<User> getUserById(String uid) {
        FirebaseQueryLiveData userLiveData = new FirebaseQueryLiveData(
                firebaseDatabase.getReference(PATH_USERS).child(uid));
        return Transformations.map(userLiveData, new ItemDeserializer<>(User.class));
    }

    public Task<Void> updateUser(User user) {
        User userNoId = new User(user);
        userNoId.setUid(null);
        return firebaseDatabase.getReference(PATH_USERS).child(user.getUid())
                .setValue(userNoId);
    }

    public Task<Void> addTopic(Topic topic) {
        return firebaseDatabase.getReference(PATH_TOPICS)
                .push().setValue(topic);
    }

    public LiveData<List<Topic>> getAllTopics() {
        FirebaseQueryLiveData allTopicsLiveData = new FirebaseQueryLiveData(
                firebaseDatabase.getReference(PATH_TOPICS));
        return Transformations.map(allTopicsLiveData, new ListDeserializer<>(Topic.class));
    }

    public LiveData<List<Topic>> getUserTopics(String uid) {
        FirebaseQueryLiveData userTopicsLiveData = new FirebaseQueryLiveData(
                firebaseDatabase.getReference(PATH_USER_TOPICS).child(uid));
        return Transformations.map(userTopicsLiveData, new ListDeserializer<>(Topic.class));
    }

    public Task<Void> followTopic(String userUid, Topic topic) {
        return firebaseDatabase.getReference(PATH_USER_TOPICS)
                .child(userUid).child(topic.getUid())
                .setValue(topic);
    }

    public Task<Void> unfollowTopic(String userUid, Topic topic) {
        return firebaseDatabase.getReference(PATH_USER_TOPICS)
                .child(userUid).child(topic.getUid())
                .removeValue();
    }

    public Task<Void> addQuestion(Question question) {
        return firebaseDatabase.getReference(PATH_TOPIC_QUESTIONS)
                .child(question.getTopicId())
                .push().setValue(question);
    }

    public Task<Void> followQuestion(String questionId, String userId) {
        return firebaseDatabase.getReference(PATH_QUESTION_USERS)
                .child(questionId)
                .child(userId)
                .setValue(true);
    }

    public Task<Void> unFollowQuestion(String questionId, String userId) {
        return firebaseDatabase.getReference(PATH_QUESTION_USERS)
                .child(questionId)
                .child(userId)
                .setValue(false);
    }

    public LiveData<List<Question>> getQuestionsByTopic(String uid) {
        FirebaseQueryLiveData questionsLiveData = new FirebaseQueryLiveData(
                firebaseDatabase.getReference(PATH_TOPIC_QUESTIONS).child(uid));
        return Transformations.map(questionsLiveData, new ListDeserializer<>(Question.class));
    }

    public Task<Void> addAnswer(Answer answer) {
        return firebaseDatabase.getReference(PATH_ANSWERS)
                .push().setValue(answer);
    }

    public LiveData<List<Answer>> getAllAnswers() {
        FirebaseQueryLiveData answersLiveData = new FirebaseQueryLiveData(
                firebaseDatabase.getReference(PATH_ANSWERS));
        return Transformations.map(answersLiveData, new ListDeserializer<>(Answer.class));
    }

    public LiveData<List<Answer>> getAnswersByUser(String uid) {
        FirebaseQueryLiveData answersLiveData = new FirebaseQueryLiveData(
                firebaseDatabase.getReference(PATH_ANSWERS)
                        .orderByChild(FIELD_USER_ID).equalTo(uid));
        return Transformations.map(answersLiveData, new ListDeserializer<>(Answer.class));
    }

    public LiveData<List<Answer>> getAnswersByTopic(String uid) {
        FirebaseQueryLiveData answersLiveData = new FirebaseQueryLiveData(
                firebaseDatabase.getReference(PATH_ANSWERS)
                        .orderByChild(FIELD_TOPIC_ID).equalTo(uid));
        return Transformations.map(answersLiveData, new ListDeserializer<>(Answer.class));
    }

    public LiveData<List<Answer>> getAnswersByQuestion(String uid) {
        FirebaseQueryLiveData answersLiveData = new FirebaseQueryLiveData(
                firebaseDatabase.getReference(PATH_ANSWERS)
                        .orderByChild(FIELD_QUESTION_ID).equalTo(uid));
        return Transformations.map(answersLiveData, new ListDeserializer<>(Answer.class));
    }

    public LiveData<List<Answer>> getNotificationsForUser(String uid) {
        FirebaseQueryLiveData answersLiveData = new FirebaseQueryLiveData(
                firebaseDatabase.getReference(PATH_USER_NOTIFICATIONS).child(uid));
        return Transformations.map(answersLiveData, new ListDeserializer<>(Answer.class));
    }
}
