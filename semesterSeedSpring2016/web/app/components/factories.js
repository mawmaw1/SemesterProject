'use strict';

/* Place your global Factory-service in this file */

angular.module('myApp.factories', []).
        factory('InfoFactory', function () {
            var user1 = {};
            var info = "Hello World from a Factory";
            var getInfo = function getInfo() {
                return info;
            };

            var setUser = function setUser(user) {
                console.log(user);
                user1 = user;
            };

            var getUser = function getUser() {
                return user1;
            };

            return {
                getInfo: getInfo,
                setUser: setUser,
                getUser: getUser
            };
        });