#!/usr/bin/env python
#coding:utf-8

import pandas
import numpy as np
from sklearn.ensemble import RandomForestClassifier
from sklearn import cross_validation

train = pandas.read_csv("fing_nexus5x_1.csv")
test  = pandas.read_csv("fing4.csv")

def clean(data):
    # data.loc[data["Finger"] == "Thumb", "Finger"]       = 0
    # data.loc[data["Finger"] == "Index", "Finger"]       = 1
    # data.loc[data["Finger"] == "SecondJoint", "Finger"] = 2

    data.loc[data["Finger"] == "Thumb", "Finger"]       = "Index"
    # data = data[data["Type"] == 2]
    return data

train = clean(train)
test  = clean(test)

want = "Finger"
predictors = ["Pressure", "Size", "Major", "Minor"]
alg = RandomForestClassifier(random_state=1, n_estimators=150, min_samples_split=4)
alg.fit(train[predictors], train[want])

predictions = alg.predict(test[predictors])
sc = 0

for exp, ans in zip(predictions, test[want]):
    sc += (exp == ans)
print(1.0 * sc / len(test[want]))

