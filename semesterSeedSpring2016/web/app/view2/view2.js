
var app = angular.module('myApp.view2', ['ngRoute', 'ui.bootstrap']);

app.config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/view2', {
            templateUrl: 'app/view2/view2.html',
            controller: 'View2Ctrl',
            controllerAs: 'ctrl'
        });

    }]);

app.controller('View2Ctrl', ['GetFactory', '$http', function (GetFactory, $http) {
        var self = this;


        self.opt = [
            'CPH',
            'STN',
            'SXF',
            'CDG',
            'BCN'
        ];


        self.getAllFlightsFromDate = (function (from, to, date, persons) {

            self.showme = false;
            if (to !== from && from !== undefined) {

                date.setHours(date.getHours() - date.getTimezoneOffset() / 60);

                var jsonDate = date.toISOString();

                if (to === undefined) {


                    GetFactory.getAllFlightsFromDate(from, to, jsonDate, persons).then(function successCallback(res) {
                        self.showme = true;
                        self.data = [];
                        for (var i = 0; i < res.data.length; i++) {
                            if (res.data[i].error === undefined) {
                                self.data.push(res.data[i]);
                            }
                        }
                    }, function errorCallback(res) {
                        self.error = res.status + ": " + res.data.statusText;
                    });
                } else {
                    GetFactory.getAllFlightsFromToDate(from, to, jsonDate, persons).then(function successCallback(res) {
                        self.showme = true;
                        self.data = res.data;

                    }, function errorCallback(res) {
                        self.error = res.status + ": " + res.data.statusText;
                    });
                }
            } else {
                alert("You are dumb");
            }
        });




    }]);

app.factory('GetFactory', ['$http', function ($http) {
        var self = this;

        var getAllFlightsFromDate = (function (from, to, date, persons) {
            return getAllFlightsFromDate =
                    $http({
                        method: 'GET',
                        url: 'http://localhost:8080/SemesterProjectServer/api/data/' + from + '/' + date + '/' + persons

                    });
        });
        var getAllFlightsFromToDate = (function (from, to, date, persons) {
            return getAllFlightsFromToDate =
                    $http({
                        method: 'GET',
                        url: 'http://localhost:8080/SemesterProjectServer/api/data/' + from + '/' + to + '/' + date + '/' + persons

                    });
        });
        return {
            getAllFlightsFromDate: getAllFlightsFromDate,
            getAllFlightsFromToDate: getAllFlightsFromToDate

        };


    }]);
