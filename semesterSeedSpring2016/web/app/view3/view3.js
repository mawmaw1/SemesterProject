'use strict';

var app = angular.module('myApp.view3', ['ngRoute']);

app.config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/view3', {
            templateUrl: 'app/view3/view3.html',
            controller: 'View3Ctrl',
            controllerAs: 'ctrl'
        });
    }]);

app.controller('View3Ctrl', ["$http", function ($http) {
        var self = this;
        self.showUsers = false;
        self.showReservations = false;
        self.getUsers = function (){
            $http.get('api/admin/users')
                .success(function (data, status, headers, config) {
                    console.log(data);
                    self.data = data;
                })
                .error(function (data, status, headers, config) {

                });
        };
        self.getUsers(); 
        
        self.getReservations = function (){
            $http.get('api/admin/reservations')
                .success(function (data, status, headers, config) {
                    console.log(data);
                    self.data2 = data;
                })
                .error(function (data, status, headers, config) {

                });
        };
        self.getReservations();
        
        self.deleteUser = function (username){
            $http.delete("api/admin/user/"+username).then(function (res){
                console.log(res);
                alert(res.data.userName + " has been deleted");
                self.getUsers();
            });
        };
        self.deleteReservation = function (id){
            $http.delete("api/admin/reservation/"+id).then(function (res){
                console.log(res);
                alert("Reservation with id " + res.data.id + " from " + res.data.airlineName + " has been deleted");
                self.getReservations();
            });
        };
        
        self.showUsersFunc = function (){
           self.showReservations = false;
           self.showUsers = true;
        };
     
        self.showReservationsFunc = function (){          
           self.showUsers = false;
           self.showReservations = true;
        };
    }]);

app.filter("roleFilter", [function () {
        return function (inputItem) {
            var res = "";
            for (var i = 0; i < inputItem.length; i++) {
                if (inputItem[i] === inputItem[inputItem.length-1]) {
                    res += inputItem[i].role;
                }
                else {
                    res += inputItem[i].role + ", ";
                }
                
            }
            return res;

        };
    }]);
