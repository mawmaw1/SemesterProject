'use strict';

var app = angular.module('myApp.view6', ['ngRoute']);

app.config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/view6', {
            templateUrl: 'app/view6/view6.html',
            controller: 'View6Ctrl',
            controllerAs: 'ctrl'
        });
    }]);

app.controller('View6Ctrl', ["$http", "$location", "GetFactory", "$scope", "InfoFactory", function ($http, $location, GetFactory, $scope, InfoFactory) {
        var self = this;
        var user = InfoFactory.getUser();
        console.log(user.username);
        self.getAllReservations = function (username) {
            GetFactory.getAllReservations(username).then(function successCallback(res) {
                self.data = res.data;
                console.log(self.data);
            }, function errorCallback(res) {
                self.error = res.status + ": " + res.data.statusText;
            });
        };
        self.getAllReservations(user.username);


    }]);

app.filter("pasFilter", [function () {
        return function (inputItem) {

            var res = "";
            if (inputItem) {

                for (var i = 0; i < inputItem.length; i++) {
                    if (inputItem[i] === inputItem[inputItem.length - 1]) {
                        res += inputItem[i].firstName + " " + inputItem[i].lastName;
                    }
                    else {
                        res += inputItem[i].firstName + " " + inputItem[i].lastName + ", ";
                    }

                }
                return res;
            }
        };
    }]);

app.factory('GetFactory', ['$http', function ($http) {
        var self = this;

        var getAllReservations = (function (username) {
            return getAllReservations =
                    $http({
                        method: 'GET',
                        url: 'api/data/reservation/' + username
                    });
        });

        return {
            getAllReservations: getAllReservations
        };


    }]);