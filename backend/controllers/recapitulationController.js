const responseView = require('../views/responseView');
const path = require('path');
const fs = require('fs');
const moment = require('moment');
const presensiModel = require('../models/presensiModel');
const userModel = require('../models/userModel');




exports.getRecapitulation = async (req, res) => {
  const { email } = req.query;
console.log(email);
  if (!email) {
      return responseView.badRequest(res, 'user id must be filled!!!');
  }

  try {
    // const presensiData = await presensiModel.getPresensi({name});

    // responseView.success(res, 'Presensi submitted successfully', presensiData);

    // const data = {
    //     title: 'Recapitulation Report',
    //     items: [
    //         { id: 1, name: 'John Doe', score: 95 },
    //         { id: 2, name: 'Jane Smith', score: 88 },
    //     ],
    // };
    // res.render('recapitulation', data);
    //------------------------------------
    const user = await userModel.findUserByEmail(email);
    if(!user){
      return responseView.badRequest(res, 'User Not Found');
    }
    console.log(user);
    const currentMonth = moment().format('MMMM-YYYY');
    const yearMonth = moment().format('YYYY-MM');
    console.log(yearMonth);
    const daysInMonth = moment().daysInMonth();
    // console.log(user.id);
    const presensiData = await presensiModel.getMonthlyPresensi({ userId: user.id, date: yearMonth });
    const dayNow = moment().format('D');
    const attendanceData = [];
    const yearMonthFormatted = moment().format('YYYY / MMMM');



    for (let day = 1; day <= daysInMonth; day++) {
      const date = moment(`${yearMonth}-${String(day).padStart(2, '0')}`).format('D');

      if (day > dayNow) {
        let hadir = '';
        let izin = '';
        let keterangan = '';
        let absen = '';
        attendanceData.push({
          date,
          hadir,
          izin,
          absen,
          keterangan,
        });
        continue;
      }
      const presensiForDay = presensiData.find(p => parseInt(p.day) === day);
      // Inisialisasi data default
      let hadir = '';
      let izin = '';
      let absen = true; // Default ke absen
      let keterangan = '';

      // Periksa jika ada presensi untuk hari tersebut
      if (presensiForDay) {
        absen = false; // Tidak absen karena ada data
        if (presensiForDay.type === '1') {
          hadir = true;
        } else if (presensiForDay.type === '2') {
          izin = true;
        } else {
          absen = true; // Jika bukan hadir atau izin, tetap absen
        }
        keterangan = presensiForDay.description || '';
      }

      attendanceData.push({
        date,
        hadir,
        izin,
        absen,
        keterangan,
      });
    }
    const baseUrl = `${req.protocol}://${req.get('host')}`;
    res.render('recapitulation', { currentMonth, attendanceData, baseUrl, yearMonthFormatted, user});

    //------------------------------------

    // const filePath = path.join(__dirname, '..','views', 'recapitulation.html');

    // fs.readFile(filePath, 'utf8', (err, html) => {
    //     if (err) {
    //         return res.status(500).send('Error reading HTML file');
    //     }

    //     // responseView.success(res, 'Successfully get recapitulation data', html);
    //     res.send(html);
    // });


  } catch (err) {
    responseView.error(res, 'failed get recapitulation data', err.message);
  }



}

exports.getReportPerformance = async (req, res) => {
  console.log(req.query)
    const {userId} = req.query;
    if (!userId) {
      console.log(userId);
        return responseView.badRequest(res, 'User Ids must be filled!!!');
    }

    try {
      const date = moment().format('YYYY-MM-DD');
      console.log(date);
        const report = await presensiModel.getReportPerformance({userId, date});
  
        responseView.success(res, 'Get Report Performance successfully', report);
      } catch (err) {
        responseView.error(res, 'Error get performance', err.message);
      }



  }