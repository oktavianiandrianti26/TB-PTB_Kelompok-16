const express = require('express');
const router = express.Router();
const userController = require('../controllers/userController');
const presensiController = require('../controllers/presensiController');
const recapitulationController = require('../controllers/recapitulationController');
const taskController = require('../controllers/taskController');
const inventoryController = require('../controllers/inventoryController');


router.post('/register', userController.register);
router.post('/login', userController.login);

router.post('/presensi', presensiController.submitPresensi);
router.post('/izin', presensiController.submitIzin);
router.get('/presensi', presensiController.getPresensi);
router.get('/permit', presensiController.getPermit);

router.get('/recapitulation', recapitulationController.getRecapitulation);

router.get('/profile', userController.getUserProfile);
router.post('/profile', userController.updateProfile);

router.get('/dailytask', taskController.getDailyTask);
router.post('/createTask', taskController.createTask);
router.post('/createInventory', inventoryController.createInventory);
router.get('/get-report', recapitulationController.getReportPerformance);


module.exports = router;