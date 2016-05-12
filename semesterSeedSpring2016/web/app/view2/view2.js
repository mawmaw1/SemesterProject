
var app = angular.module('myApp.view2', ['ngRoute', 'ui.bootstrap']);

app.config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/view2', {
            templateUrl: 'app/view2/view2.html',
            controller: 'View2Ctrl',
            controllerAs: 'ctrl'
        });

    }]);

app.controller('View2Ctrl', ['GetFactory1', 'InfoFactory', '$http', function (GetFactory1, InfoFactory, $http) {
        var self = this;
        self.repeat;

        self.reservation = {};
        self.reservation.passengers = [];
        self.showSearch = true;
        self.showBooking = false;
        self.opt = [
            'CPH',
            'STN',
            'SXF',
            'CDG',
            'BCN'
        ];

        self.bookTicketsInfo = function (flight, airlineName) {
            console.log(flight);
            console.log(airlineName);
            var currentUser = InfoFactory.getUser();
            if ((Object.getOwnPropertyNames(currentUser).length === 0) === true) {
                alert("You have to log in to book tickets");
            } else {
                self.flight = flight;
                self.showme = false;
                self.showSearch = false;
                self.showBooking = true;
                self.showSucces = false;
                self.reservation.flightID = self.flight.flightID;
                self.reservation.numberOfSeats = self.flight.numberOfSeats;
                self.repeat = self.flight.numberOfSeats;
                self.airlineName = airlineName;
            }
        };

//        self.getRepeat = function (number){
//            return new Array(number);
//        };

        self.bookTickets = function () {
            var currentUser = InfoFactory.getUser();
            console.log(self.reservation);

            GetFactory1.bookTickets(self.reservation, self.airlineName, self.flight.flightID).then(function successCallback(res) {
                console.log(res.data);
                self.reservationResponse = res.data;
                self.reservationResponse.totalPrice = self.flight.totalPrice;
                self.reservationResponse.user = currentUser;
                self.reservationResponse.airlineName = self.airlineName;
                console.log(self.reservationResponse);
                GetFactory1.saveReservation(self.reservationResponse).then(function successCallback(res) {
                    console.log("succes");
                }, function errorCallback(res) {
                    self.error = res.status + ": " + res.data.statusText;
                });


            }, function errorCallback(res) {
                self.error = res.status + ": " + res.data.statusText;
            });

            self.showBooking = false;
            self.showSucces = true;

        };

        self.addPassenger = function () {
            if (self.repeat === 0) {
                alert("You cannot add more passengers than number of seats");
                self.passenger = {};
            } else {
                self.reservation.passengers.push(self.passenger);
                self.passenger = {};
                self.repeat = self.repeat - 1;
            }
        };


        self.getAllFlightsFromDate = (function (from, to, date, persons) {
            self.searchData = {};
            self.searchData.origin = from;
            self.searchData.destination = to;
            self.searchData.date = date;
            self.searchData.numberOfSeats = persons;
            GetFactory1.saveSearchData(self.searchData).then(function successCallback(res) {
                console.log(res.data);
            }, function errorCallback(res) {
                self.error = res.status + ": " + res.data.statusText;
            });

            self.showme = false;
            if (to !== from && from !== undefined) {

                date.setHours(date.getHours() - date.getTimezoneOffset() / 60);

                var jsonDate = date.toISOString();

                if (to === undefined) {

                    GetFactory1.getAllFlightsFromDate(from, to, jsonDate, persons).then(function successCallback(res) {
                        self.showme = true;
                        self.data = [];
                        console.log(res.data);
//                        for (var i = 0; i < res.data.length; i++) {
//                            if (res.data[i].error === undefined) {
//                                self.data.push(res.data[i]);
//                            }
//                        }
                        self.data = res.data;
                    }, function errorCallback(res) {
                        self.error = res.status + ": " + res.data.statusText;
                    });
                } else {
                    GetFactory1.getAllFlightsFromToDate(from, to, jsonDate, persons).then(function successCallback(res) {
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

app.factory('GetFactory1', ['$http', function ($http) {
        var self = this;

        var getAllFlightsFromDate = (function (from, to, date, persons) {
            return getAllFlightsFromDate =
                    $http({
                        method: 'GET',
                        url: 'api/data/' + from + '/' + date + '/' + persons

                    });
        });
        var getAllFlightsFromToDate = (function (from, to, date, persons) {
            return getAllFlightsFromToDate =
                    $http({
                        method: 'GET',
                        url: 'api/data/' + from + '/' + to + '/' + date + '/' + persons

                    });
        });

        var bookTickets = (function (details, airline, flightID) {
            return bookTickets =
//                    $http.post('api/data/reservation/airline', details);
                    $http.post('api/data/reservation/' + airline + "/" + flightID, details);
        });

        var saveReservation = (function (reservation) {
            return saveReservation =
                    $http.post('api/data/reservation', reservation);
        });

        var saveSearchData = (function (searchData) {
            return saveSearchData =
                    $http.post('api/data/searchData', searchData);
        });

        return {
            getAllFlightsFromDate: getAllFlightsFromDate,
            getAllFlightsFromToDate: getAllFlightsFromToDate,
            bookTickets: bookTickets,
            saveReservation: saveReservation,
            saveSearchData: saveSearchData
        };


    }]);
