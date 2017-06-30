var treeApp = angular.module('TreeApp', [ 'treeControl', 'ngMaterial' ]);

treeApp.controller('TreeController', function TreeController($scope, $mdDialog, $http) {

	$scope.treeOptions = {
		nodeChildren : "children",
		dirSelectable : true,
		multiSelection : false,
		injectClasses : {
			ul : "a1",
			li : "a2",
			liSelected : "a7",
			iExpanded : "a3",
			iCollapsed : "a4",
			iLeaf : "a5",
			label : "a6",
			labelSelected : "a8"
		}
	}
	
	$scope.dataForTheTree = [];

	function loadItems() {		
		$http.get('/tree/rest/treeNode/items', {}).then(function successCallback(response) {
			$scope.dataForTheTree = response.data;
			$scope.expandAll();
		}, function errorCallback(response) {
			showAlert("Ocorreu um erro ao carregar os dados! Tente novamente!");
		});
	}
	
	loadItems();
	
	$scope.formItem = {};
	
	$scope.collapseAll = function() {
		$scope.expandedNodes = [];
	};

	$scope.expandAll = function() {
		$scope.expandedNodes = [];

		for (var int = 0; int < $scope.dataForTheTree.length; int++) {
			var item = $scope.dataForTheTree[int];
			checkForExpand(item);
		}
	};
	
	function checkForExpand(item) {
		if (item.hasOwnProperty("children") && item.children.length > 0) {
			$scope.expandedNodes.push(item);
			for (var int = 0; int < item.children.length; int++) {
				var child = item.children[int];
				checkForExpand(child);
			}
		}
	}
	
	confirmRemove = function(ev) {
		
		if ($scope.selected == undefined) {
			showAlert("Selecione um produto para remoção!");
			return;
		}
		
	    var confirm = $mdDialog.confirm()
	          .title('Tem certeza que deseja remover o produto e seus dependentes?')
	          .targetEvent(ev)
	          .ok('Remover!')
	          .cancel('Cancelar');

	    $mdDialog.show(confirm).then(function() {
	    	$http.delete('/tree/rest/treeNode/' + $scope.selected.id, $scope.formItem, {}).then(function successCallback(response) {
				if (response.data == true) {
					loadItems();
					$scope.expandAll();
					$mdDialog.hide();
					showAlert("Produto removido com sucesso!");
				} else {
					showAlert("Ocorreu um erro! Tente novamente!");
				}
			}, function errorCallback(response) {
				showAlert("Ocorreu um erro! Tente novamente!");
			});
	    }, function() {
	      // nothing to do.
	    });
	};
	
	showPrerenderedDialog = function(ev) {
	    $mdDialog.show({
	      contentElement: '#myDialog',
	      parent: angular.element(document.body),
	      targetEvent: ev,
	      clickOutsideToClose: true
	    });
	  };
	
	//modal confirmando e request
	$scope.remove = function(ev) {
		confirmRemove();
	};
	
	//form modal
	$scope.edit = function(ev) {
		$scope.formTitle = "Editar Produto";
		$scope.formItem = $scope.selected;
		$scope.requestMethod = "put";
		showPrerenderedDialog(ev);
		console.log($scope.selected);
	};
	
	//form modal
	$scope.add = function(ev) {
		$scope.formTitle = "Adicionar Produto";
		$scope.requestMethod = "post";
		$scope.formItem = {};
		showPrerenderedDialog(ev);
	};
	
	$scope.saveForm = function() {
		$scope.errorMsg = "";
		if ($scope.selected != undefined) {
			$scope.formItem.parent = {};
			$scope.formItem.parent.id = $scope.selected.id;
		}
		
		if (!$scope.formItem.code || !$scope.formItem.description) {
			$scope.errorMsg = "Preencha os campos marcados como obrigatórios \"*\"!";
			return;
		}
		
		$http[$scope.requestMethod]('/tree/rest/treeNode', $scope.formItem, {}).then(function successCallback(response) {
			if (response.data == true) {
				loadItems();
				$scope.expandAll();
				$mdDialog.hide();
				showAlert("Produto Salvo com sucesso!");
			} else {
				showAlert("Ocorreu um erro! Tente novamente!");
			}
		}, function errorCallback(response) {
			showAlert("Ocorreu um erro! Tente novamente!");
		});
	};
	
	showAlert = function(title) {
	    $mdDialog.show(
	      $mdDialog.alert()
	        .parent(angular.element(document.querySelector('#popupContainer')))
	        .clickOutsideToClose(true)
	        .title(title)
	        .ok('ok')
	    );
	  };

});