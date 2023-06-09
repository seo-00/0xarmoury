import React, {useEffect, useState} from 'react';
// import {BrowserRouter as Router, Routes, Route, Link, Switch} from 'react-router-dom';  // eslint-disable-line no-unused-vars 
import axios from 'axios';
import Button from '@mui/material/Button';
import './mainPage.css';
import './Training.css';
import Box from '@mui/material/Box';
import InputAdornment from '@mui/material/InputAdornment';
import SearchIcon from '@mui/icons-material/Search';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Card from '@mui/material/Card';
import CardActions from '@mui/material/CardActions';
import CardContent from '@mui/material/CardContent';
import Typography from '@mui/material/Typography';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import SchoolIcon from '@mui/icons-material/School';
import { textAlign } from '@mui/system';

const categoryList = [
    {id: '1',
     label: 'Information Gathering',},
    {id: '2',
     label: 'Vulnerability Analysis',},
    {id: '3',
     label: 'Web Application Analysis',},
    {id: '4',
     label: 'Database Assessment',},
     {id: '5',
     label: 'Password Attacks',},
     {id: '6',
     label: 'Wireless Attacks',},
     {id: '7',
     label: 'Reverse Engineering',},
     {id: '8',
     label: 'Exploitation Tools',},
     {id: '9',
     label: 'Sniffing & Spoofing',},
     {id: '10',
     label: 'Post Exploitation',},
     {id: '11',
     label: 'Forensics',},
     {id: '12',
     label: 'Reporting Tools',},
  ];


function MainPage(){
    const [jsonData, setHello] = useState('');
    const [selectedId, setSelectedId] = useState(null);
    const [selectedLabel, setSelectedLabel] = useState("[Select Category]");
    const [pageCount, setPageCount] = useState(0);
    const [category, setCategory] = useState('');
    const [scenario, setScenario] = useState([]);

    const badge = localStorage.getItem('badge');

    const ITEM_HEIGHT = 48;
    const ITEM_PADDING_TOP = 8;
    const MenuProps = {
      PaperProps: {
        style: {
          maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP,
          width: 250,
        },
      },
    };

    const handleChange1 = (event) => {
        setCategory(event.target.value);
        handleSelect(event.target.value);
      };
    {/* scenarioList(event.target.value) */}

    const scenarioList = async (categoryidx) => { //!!시나리오 불러올 때 변수 바꾸기!!
        try {
            const postData = {
              "categoryIdx":categoryidx,
              "pageNum": 1
            }
            const response = await axios.post('/training/postList/', postData); //경로 변경
            
            console.log(postData);
            const processedTool = response.data.result.map(item => ({
                nickName: item.nickName, //변수 변경
                title: item.title,
                description: item.description,
                postTime: item.postTime,
                url: item.url
              }));
              setScenario(processedTool);
          } catch (error) {
            console.error('Tool:', error);
        }
    }; 

    const handleSelect = (id) => {
        setSelectedId(id);
        scenarioList(id);
        fetchData(id)
          .then((response) => {
            const { categoryName } = response.data.result[id-1];
            setSelectedLabel(categoryName);
            // setToolCode(code);
          })
          .catch((error) => {
            console.error('Error fetching data:', error);
          });
      };


    async function fetchData(id) {
        try {
          const response = await axios.get(`/tools/category`);
          return response;
        } catch (error) {
          throw error;
        }
      }

    const Logout = () => {
        localStorage.removeItem("accessToken");
        localStorage.removeItem("userId");
        localStorage.removeItem("nickName");
        localStorage.removeItem("email");
        localStorage.removeItem("badge");
    }

    return(
        <div>
            <div className='container-right'>
                <div>
                    <div>
                    {
                        localStorage.getItem("accessToken") == null
                        ?<div className="sign-container"><Link href ="./SignIn" color='#000000'>Sign In</Link>
                        <Link href="./SignUp" color='#000000'>Sign Up</Link></div>
                        :<div className="sign-container">
                            <div>
                            <SchoolIcon style={{ color: badge > 5 ? '#F15F5F' : '#6B66FF', verticalAlign: 'bottom', marginRight: 8}}/> 
                            <Link href ="./Account" color='#000000'>          
                                {localStorage.getItem('nickName')}
                            </Link>
                            </div>
                        <Link href="./" onClick={Logout} color='#000000'>logout</Link></div>
                    }
                    </div>
                </div>
            </div>
            <div className="container">
                {/*logo*/}
                <Link href ="/">    
                    <Button startIcon={<img src="/img/armoury_logo.png" alt="Armoury" width={60} height={60}/>} sx={{
                    mr:1
                    ,fontSize:30
                    ,color:"black"
                    }}>
                    <strong>0xARMOURY</strong>
                    </Button>
                </Link>

                <div className='container-right'>
                    <div className='outline-container'>
                        <div className="button-container">
                            <Link href ="./Matrix" color='#000000'>Matrix</Link>
                            <Link href="./Tools" color='#000000'>Tools</Link>
                            <Link href ="./Training" color='#0042ED'>Training</Link>
                            <Link href ="./Gallery" color='#000000'>Gallery</Link>
                            <Link href ="./Mytools" color='#000000'>My tool</Link>
                        </div>
                    </div>
                    
                    <Box sx={{ '& > :not(style)': { m: 1 } , justifyContent: "flex-end"}}>
                        <TextField
                            id="input-with-icon-textfield"
                            
                            InputProps={{
                            startAdornment: (
                                <InputAdornment position="end">
                                <SearchIcon/>
                                </InputAdornment>
                            )
                            ,
                            }}
                            variant="standard"
                        />
                    </Box>
                </div>
            </div>
            <div className = 'division-line'></div>
            <div className='container-body1' >
                <div className='training-left'>
                <div className='tools-title'>
                     SCENARIO LIST
                </div>
                    <div className = 'tool-division-line2'></div>
                    <div className='tool-box-select'> 
                        <FormControl sx={{ m: 1, width: 270, bgcolor: 'white'}}>
                            <InputLabel id="demo-simple-select-label">Category</InputLabel>
                            <Select
                            labelId="demo-simple-select-label"
                            id="demo-simple-select"
                            value={category}
                            label="Category"
                            onChange={handleChange1}
                            MenuProps={MenuProps}
                            sx={{ maxHeight: '50px', fontSize: '16px'}}
                            >
                            {categoryList.map((item) => (
                                <MenuItem value={item.id}> {item.label}</MenuItem>
                            ))}
                            </Select>
                        </FormControl>

                        </div>
                    <div className = 'tool-division-line2'></div>
                </div>
                <div className='training-right'>
                    <div className='training-box-top'>
                        <div className='training-scenario-title'>{selectedLabel}</div>
                        <Button href='./TrainingUpload' variant="outlined" sx={{m: 1, color: 'black', borderColor:"black", ":hover": { borderColor: "black" },
						boxShadow: 2,}}>Upload Your Problem</Button>
                    </div> 
                    <div className='training-division-line-top'></div>
                    <div className='training-box-content'>
                            {scenario.map((item) => ( //!!변수 변경
                                <Card sx={{ width: '30%', minHeight: '70%', height: 'auto', bgcolor: '#F6F6F6' , boxShadow: '0 0 7px rgb(151, 151, 151)', 
                                    mb:4, ml:2, mr:2, display: 'flex', justifyContent: 'space-between', flexDirection: 'column'}}>
                                    <CardContent>
                                        <Typography sx={{ fontSize: 15, textAlign:"right"}} color="text.secondary">  
                                        {item.postTime}
                                        </Typography>
                                        <Typography sx={{ fontSize: 28, fontWeight: 600 }}>
                                        {item.title}
                                        </Typography>
                                        <Typography sx={{ fontSize: 15 }} color="text.secondary">by</Typography>
                                        <Typography sx={{ fontSize: 16, fontWeight: 600, mb: 4 }} color="text.secondary">
                                        {item.nickName}
                                        </Typography>
                                        <Typography sx={{ fontSize: 15, textAlign: "left"}}>
                                        {item.description &&
                                            item.description.split('\n').map((line, index) => (
                                              <React.Fragment key={index}>
                                                {line}
                                                <br/>
                                              </React.Fragment>
                                            ))}
                                        </Typography>
                                    </CardContent>
                                    <CardActions sx={{justifyContent: 'end', mr: 1}}>
                                        <Button href={item.url} sx={{fontWeight: 600, fontSize: '16px'}}>Go To Download</Button>
                                    </CardActions>
                                </Card>
                            ))}  
                    </div>
                </div>
            </div>
        </div>
    );
}

export default MainPage;

