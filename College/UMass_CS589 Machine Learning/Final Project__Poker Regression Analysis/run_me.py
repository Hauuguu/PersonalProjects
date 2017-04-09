import numpy as np
import time
from sklearn import neighbors
from sklearn.neighbors import DistanceMetric
from sklearn.neighbors import KNeighborsRegressor
from sklearn.ensemble import RandomForestRegressor
from sklearn.ensemble import GradientBoostingRegressor
from sklearn import cross_validation
from math import sqrt
from sklearn.feature_selection import SelectKBest
np.random.seed(0)

def RMSE(true,pred): #method for computing sqrt(Mean Squared Error)
    rmse=0
    
    for i in range(len(pred)):
   	 err=pred[i]-true[i]
   	 err=err*err
   	 rmse=rmse+err
   	 
    rmse=rmse/len(pred)
    rmse=sqrt(rmse)
    return rmse

def removed_features(feature_array): #method for computing the indexes of the removed features 
    removed=[]
    for i in range(len(feature_array)):
    	if (feature_array[i]==False):
    		removed.append(i)
    return removed
        
data_train = open("finaldata.txt", "r") #open file of training data
data_test = open("testdata.txt", "r") #open file of testing data

X5cards = [] #holds the features for training set complete hands
X4cards = [] #holds the features for training set hands with 4 cards
X3cards = [] #holds the features for training set hands with 3 cards
X2cards = [] #holds the features for training set hands with 2 cards
X1cards = [] #holds the features for training set hands with 1 card
y = [] #holds the labels for all training set hands

for line in data_train: #parses and stores the training set
	line2=np.asarray(line[0:len(line)-2].split(','),dtype='i')
	X5cards.append(line2[0:10])
	X4cards.append(line2[0:8])
	X3cards.append(line2[0:6])
	X2cards.append(line2[0:4])
	X1cards.append(line2[0:2])
	y.append(line2[10])


X5cardsfinal = [] #holds the features for testing set complete hands
X4cardsfinal = [] #holds the features for testing set hands with 4 cards
X3cardsfinal = [] #holds the features for testing set hands with 4 cards
X2cardsfinal = [] #holds the features for testing set hands with 4 cards
X1cardsfinal = [] #holds the features for testing set hands with 4 cards
yfinal = [] #holds the labels for all testing set hands

for line in data_test: #parses and stores the testing set
	line2=np.asarray(line[0:len(line)-2].split(','),dtype='i')
	X5cardsfinal.append(line2[0:10])
	X4cardsfinal.append(line2[0:8])
	X3cardsfinal.append(line2[0:6])
	X2cardsfinal.append(line2[0:4])
	X1cardsfinal.append(line2[0:2])
	yfinal.append(line2[10])


for cards in range (5): #Loops through number of cards in hand, and sets the correct training and testing set to be used
	if(cards==0):
		X=X5cards
		Xfinal=X5cardsfinal
		print "\n----------------------"
		print "With 5 cards"
		print "----------------------\n"
	if(cards==1):
		X=X4cards
		Xfinal=X4cardsfinal
		print "\n----------------------"
		print "With 4 cards"
		print "----------------------\n"
	if(cards==2):
		X=X3cards
		Xfinal=X3cardsfinal
		print "\n----------------------"
		print "With 3 cards"
		print "----------------------\n"
	if(cards==3):
		X=X2cards
		Xfinal=X2cardsfinal
		print "\n----------------------"
		print "With 2 cards"
		print "----------------------\n"
	if(cards==4):
		X=X1cards
		Xfinal=X1cardsfinal
		print "\n----------------------"
		print "With 1 card"
		print "----------------------\n"

	# Use cross-validation with test size set to 20% for 80-20 split
	X_train, X_test, y_train, y_test = cross_validation.train_test_split(X, y, test_size=0.2, random_state=0)


	print "Start of KNN Regression"
	# #####################################
	#                             	 
	#  	KNN Regressor-FEATURE SELECTION  	 
	#                             	 
	# #####################################

	regressor = KNeighborsRegressor()

	bestrmse=float("inf")

	for f in range (len(X_train[0])): #Performs feature selection and keeps track of best performing features
	    skb=SelectKBest(k=f) #Uses Select-K-Best feature selection
	    X_train_new=skb.fit_transform(X_train, y_train)
	    X_test_new=skb.transform(X_test)

	    regressor = regressor.fit(X_train_new, y_train)
	    predictions=regressor.predict(X_test_new)

	    rmse = RMSE(y_test,predictions) 
	    if(rmse<bestrmse): #best features decided by the features that produces the lowest RMSE
	   	 bestrmse=rmse
	   	 bestNumFeatures=f


	# ########################################
	#                             	 
	#  	KNN Regressor-HYPERPARAM SELECTION  	 
	#                             	 
	# ########################################

	skb=SelectKBest(k=bestNumFeatures) #use best features for hyper-parameter selection
	X_train_new=skb.fit_transform(X_train, y_train)
	X_test_new=skb.transform(X_test)
	print "Best RMSE from feature selection: ",bestrmse

	print "Removed features: ", removed_features(skb.get_support()) #print features that have been removed

	bestrmse=float("inf")
	for n in range (1,50): #Performs hyper-parameter selection and keeps track of best performing hyper-paremeters
	    regressor = KNeighborsRegressor(n_neighbors=n) 
	    regressor = regressor.fit(X_train_new, y_train)
	    predictions=regressor.predict(X_test_new)

	    rmse = RMSE(y_test,predictions)
	   	 
	    if(rmse<bestrmse): #best hyperparameters decided by the hyperparameters that produce the lowest RMSE
	   	 bestrmse=rmse
	   	 bestParameters=n

	print "predicted RMSE= ",bestrmse #print predicted best RMSE based on the cross-validated training data
	# ###########################
	#                             	 
	#  	KNN Regressor-TESTING 	 
	#                             	 
	# ###########################
	Xfinal_new=skb.transform(Xfinal) #test on the origional test data using the best features and hyperparameters
	regressor = KNeighborsRegressor(n_neighbors=bestParameters) 
	regressor = regressor.fit(X_train_new, y_train)
	final_pred = regressor.predict(Xfinal_new)

	final_rmse = RMSE(yfinal,final_pred)

	print 'Final RMSE: ',final_rmse,' with: features = ',bestNumFeatures,' | neighbors = ',bestParameters
	print "End of  KNN Regression \n"

	print "Start of Random Forest Regressor"
	# ###############################################
	#                             	 
	#  	Random Forest Regressor-FEATURE SELECTION  	 
	#                             	 
	# ###############################################

	regressor = RandomForestRegressor(random_state=0) 

	bestrmse=float("inf")

	for f in range (len(X_train[0])):    #Performs feature selection and keeps track of best performing features
	    skb=SelectKBest(k=f) #Uses Select-K-Best feature selection
	    X_train_new=skb.fit_transform(X_train, y_train)
	    X_test_new=skb.transform(X_test)

	    regressor = regressor.fit(X_train_new, y_train)
	    predictions=regressor.predict(X_test_new)

	    rmse = RMSE(y_test,predictions) #best features decided by the features that produces the lowest RMSE
	    if(rmse<bestrmse):
	   	 bestrmse=rmse
	   	 bestNumFeatures=f


	# #################################################
	#                             	 
	#  	Random Forest Regressor-HYPERPARAM SELECTION  	 
	#                             	 
	# #################################################
	print "Best RMSE from feature selection: ",bestrmse
	skb=SelectKBest(k=bestNumFeatures) #use best features for hyper-parameter selection
	X_train_new=skb.fit_transform(X_train, y_train)
	X_test_new=skb.transform(X_test)

	print "Removed features: ", removed_features(skb.get_support()) #print features that have been removed

	bestrmse=float("inf")
	for n in range (1,50): #Performs hyper-parameter selection and keeps track of best performing hyper-paremeters
	    regressor = RandomForestRegressor(n_estimators=n,random_state=0) 
	    regressor = regressor.fit(X_train_new, y_train)
	    predictions=regressor.predict(X_test_new)

	    rmse = RMSE(y_test,predictions)
	   	 
	    if(rmse<bestrmse): #best hyperparameters decided by the hyperparameters that produce the lowest RMSE
	   	 bestrmse=rmse
	   	 bestParameters=n

	print "predicted RMSE = ",bestrmse #print predicted best RMSE based on the cross-validated training data
	
	# ####################################
	#                             	 
	#  Random Forest Regressor-TESTING 	 
	#                             	 
	# ####################################
	
	Xfinal_new=skb.transform(Xfinal) #test on the origional test data using the best features and hyperparameters
	regressor = RandomForestRegressor(n_estimators=bestParameters,random_state=0) 
	regressor = regressor.fit(X_train_new, y_train)
	final_pred = regressor.predict(Xfinal_new)

	final_rmse = RMSE(yfinal,final_pred)

	print 'Final RMSE: ',final_rmse,' with: features = ',bestNumFeatures,' | n_estimators = ',bestParameters
	print "End of  Random Forest Regressor \n"

	print "Start of Gradient Boosting Regressor"
	
	# ##################################################
	#                             	 
	#  	Gradient Boosting Regressor-FEATURE SELECTION  	 
	#                             	 
	# ##################################################

	regressor = GradientBoostingRegressor(random_state=0) 

	bestrmse=float("inf")

	for f in range (len(X_train[0])):    #Performs feature selection and keeps track of best performing features
	    skb=SelectKBest(k=f) #Uses Select-K-Best feature selection
	    X_train_new=skb.fit_transform(X_train, y_train)
	    X_test_new=skb.transform(X_test)

	    regressor = regressor.fit(X_train_new, y_train)
	    predictions=regressor.predict(X_test_new)

	    rmse = RMSE(y_test,predictions) #best features decided by the features that produces the lowest RMSE
	    if(rmse<bestrmse):
	   	 bestrmse=rmse
	   	 bestNumFeatures=f

	# ######################################################
	#                             	 
	#  	Gradient Boosting Regressor-HYPERPARAM SELECTION  	 
	#                             	 
	# ######################################################
	
	print "Best RMSE from feature selection: ",bestrmse
	skb=SelectKBest(k=bestNumFeatures) #use best features for hyper-parameter selection
	X_train_new=skb.fit_transform(X_train, y_train)
	X_test_new=skb.transform(X_test)

	print "Removed features: ", removed_features(skb.get_support()) #print features that have been removed

	bestrmse=float("inf")
	for n in range (1,50): #Performs hyper-parameter selection and keeps track of best performing hyper-paremeters
	    regressor = GradientBoostingRegressor(n_estimators=n,random_state=0) 
	    regressor = regressor.fit(X_train_new, y_train)
	    predictions=regressor.predict(X_test_new)

	    rmse = RMSE(y_test,predictions)
	   	 
	    if(rmse<bestrmse): #best hyperparameters decided by the hyperparameters that produce the lowest RMSE
	   	 bestrmse=rmse
	   	 bestParameters=n

	print "predicted RMSE = ",bestrmse #print predicted best RMSE based on the cross-validated training data
	
	# ###################################
	#                             	 
	#  Random Forest Regressor-TESTING 	 
	#                             	 
	# ###################################
	
	Xfinal_new=skb.transform(Xfinal) #test on the origional test data using the best features and hyperparameters
	regressor = GradientBoostingRegressor(n_estimators=bestParameters,random_state=0) 
	regressor = regressor.fit(X_train_new, y_train)
	final_pred = regressor.predict(Xfinal_new)

	final_rmse = RMSE(yfinal,final_pred)

	print 'Final RMSE: ',final_rmse,' with: features = ',bestNumFeatures,' | n_estimators = ',bestParameters
	print "End of  Gradient Boosting Regressor \n"