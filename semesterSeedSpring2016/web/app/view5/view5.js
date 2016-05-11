'use strict';

angular.module('myApp.view5', ['ngRoute'])

        .config(['$routeProvider', function ($routeProvider) {
                $routeProvider.when('/view5', {
                    templateUrl: 'app/view5/view5.html',
                    controller: 'View5Ctrl',
                    controllerAs: 'ctrl'
                });
            }])

        .controller('View5Ctrl', ["$http", "$location", function ($http, $location) {
                var self = this;

                self.adduser = function () {

                    $http.post("api/data/create", self.user).success(function () {
                        console.log(self.user);
                        self.user = {};
                        $location.path('/app/view1.html');
                    });

                };



            }]);


