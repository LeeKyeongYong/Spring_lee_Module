import formidable from 'formidable';
import path from 'path';
import AWS from 'aws-sdk';

export const config = {
  api: {
    bodyParser: false,
  },
};

// AWS S3 클라이언트 설정
const s3 = new AWS.S3({
  accessKeyId: process.env.AWS_ACCESS_KEY_ID,
  secretAccessKey: process.env.AWS_SECRET_ACCESS_KEY,
  region: process.env.AWS_REGION,
});

export default async (req, res) => {
  const form = new formidable.IncomingForm({
    keepExtensions: true,
  });

  form.parse(req, async (err, fields, files) => {
    if (err) {
      return res.status(500).json({ error: 'File upload failed' });
    }

    const file = files.image[0];
    const fileStream = fs.createReadStream(file.filepath);

    // S3 업로드 매개변수 설정
    const uploadParams = {
      Bucket: process.env.AWS_S3_BUCKET_NAME,
      Key: path.basename(file.filepath), // S3 객체 키
      Body: fileStream,
      ContentType: file.mimetype,
    };

    try {
      // S3에 파일 업로드
      const data = await s3.upload(uploadParams).promise();
      const fileUrl = data.Location; // 업로드된 파일의 URL

      res.status(200).json({ fileUrl });
    } catch (error) {
      res.status(500).json({ error: 'Failed to upload file to S3' });
    }
  });
};