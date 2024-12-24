const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');
const multer = require('multer');


const app = express();
const port = 3000;
const userRoutes = require('./routes/userRoutes');

app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'html');
app.engine('html', require('ejs').renderFile); 
app.use((req, res, next) => {
    res.locals.baseUrl = `${req.protocol}://${req.get('host')}`;
    next();
});

app.use('/assets', express.static(path.join(__dirname, 'assets'), {
    setHeaders: (res, path) => {
        console.log(`Serving static file: ${path}`);
    }
}));app.use(express.json());
app.use('/api', userRoutes);
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use('/uploads', express.static(path.join(__dirname, 'uploads'))); 


app.listen(port, () => console.log(`Server running on port ${port}`));
