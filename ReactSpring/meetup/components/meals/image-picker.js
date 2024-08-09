'use client';

import classes from './image-picker.module.css';
import { useRef,useState } from 'react';
import Image from 'next/image';

export default function ImagePicker({label,name}){

    const imageInput=useRef();

    const [pickedImage, setPickedImage] = useState(null);

    function handleImageChange(event){
        const file =event.target.files[0];

        if(!file){
            setPickedImage(null);
            return;
        }

        const fileReader = new FileReader();
        fileReader.onload = ()=>{
            setPickedImage(fileReader.result);
        };
        fileReader.readAsDataURL(file);

    }

    function handlePicker(){
        imageInput.current.click();
    }

    return (
            <div className={classes.picker}>
                <label htmlFor={name}>{label}</label>
                <div className={classes.preview}>
                    {!pickedImage && <p>No image picked yet</p>}
                    {pickedImage && <Image src={pickedImage} alt="The image selected by the" fill/>}
                </div>
                <div className={classes.controls}>
                    <input 
                    className={classes.input}
                    type="file" 
                    accept="image/png, image/jpeg"
                    id={name}
                    name={name}
                    ref={imageInput}
                    onChange={handleImageChange}
                   
                     style={{ display: 'none' }} // Hide the actual file input
                    />
                    <button className={classes.button} type="button" onClick={handlePicker}>
                        pick and Image
                    </button>
                </div>
            </div>
           );
}