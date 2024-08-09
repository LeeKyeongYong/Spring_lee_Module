import multer from 'multer';
import path from 'path';
import fs from 'fs';

const upload = multer({
  storage: multer.diskStorage({
    destination: (req, file, cb) => {
      const uploadPath = path.join(process.cwd(), 'public/images');
      if (!fs.existsSync(uploadPath)) {
        fs.mkdirSync(uploadPath, { recursive: true });
      }
      cb(null, uploadPath);
    },
    filename: (req, file, cb) => {
      // 파일 이름을 UTF-8로 디코딩
      const fileName = decodeURIComponent(file.originalname);
      cb(null, fileName);
    }
  })
});

export const config = {
  api: {
    bodyParser: false,
  },
};

export default (req, res) => {
  const uploadHandler = upload.single('image');

  uploadHandler(req, res, (err) => {
    if (err) {
      console.error('Error uploading file:', err);
      return res.status(500).json({ error: 'File upload failed' });
    }

    if (!req.file) {
      return res.status(400).json({ error: 'No file uploaded' });
    }

    const fileUrl = `/images/${req.file.filename}`;
    res.status(200).json({ fileUrl });
  });
};